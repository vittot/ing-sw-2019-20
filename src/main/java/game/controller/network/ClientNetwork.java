package game.controller.network;

import game.controller.ClientController;
import game.controller.commands.ClientMessage;
import game.controller.commands.ServerGameMessage;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Network layer for the client
 */
public abstract class ClientNetwork {

    /** Reference to the ClientController     */
    protected ClientController controller;
    /** Number of ping lost from the server*/
    int nPingLost;
    /** Timer for the count of lost pings **/
    ScheduledExecutorService disconnectionExecutor;
    /** Time to wait a ping before before increment nPingLost */
    private static final int WAIT_TIME_PING_MS = 40000;
    /** True when the connection has to be closed */
    boolean stop;
    /** List of gameMessages to be processed */
    LinkedBlockingQueue<ServerGameMessage> gameMessages;
    /** Thread which process messages in queue*/
    Thread processor;

    /**
     * Send a message to the server
     * @param msg message
     */
    public abstract void sendMessage(ClientMessage msg);


    /**
     * Open the connection
     * @return true in case of success, false in case of error
     */
    public abstract boolean init();

    /**
     * Close the connection
     */
    public abstract void close();

    ClientNetwork() {
        this.nPingLost  = 0;
        this.stop = true;
        this.gameMessages = new LinkedBlockingQueue<>();
    }

    /**
     *
     * @param handler the ClientController
     */
    public void startListening(ClientController handler) {
        this.controller = handler;
        this.disconnectionExecutor = Executors.newSingleThreadScheduledExecutor();
        processor = new Thread(
                () -> {
                    do {
                        ServerGameMessage msg = null;
                        try {
                            msg = gameMessages.poll(1, TimeUnit.MINUTES);
                            if(msg != null) //it's null if timeout is elapsed
                                msg.handle(controller);
                        } catch (InterruptedException e) {
                            return;
                        }

                    }while(!stop);
                }
        );
        processor.setName("PROCESSOR THREAD");
        processor.start();
        waitNextPing();
    }

    /**
     * Put the disconnectionExecutor waiting for next ping within 60 seconds
     */
    void waitNextPing()
    {
        Runnable task = () -> {
            nPingLost++;
            if(nPingLost == 5)
            {
                System.out.println("I DIDN'T RECEIVE 5 PINGS FROM THE SERVER =(");
                controller.manageConnectionError();
                disconnectionExecutor.shutdownNow();
            }
        };
        disconnectionExecutor = Executors.newSingleThreadScheduledExecutor();
        disconnectionExecutor.scheduleAtFixedRate(task,WAIT_TIME_PING_MS,WAIT_TIME_PING_MS, TimeUnit.MILLISECONDS);
    }

    /**
     * Interrupt the scheduled executor which is called in case of missing ping
     */
    public void stopWaitingPing()
    {   //stop = true;
        if(disconnectionExecutor != null)
            disconnectionExecutor.shutdownNow();
    }


}
