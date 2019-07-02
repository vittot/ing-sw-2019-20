package game.controller;

import game.controller.commands.ClientMessage;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Client {

    protected ClientController controller;
    int nPingLost;
    ScheduledExecutorService disconnectionExecutor;
    static final int WAIT_TIME_PING_MS = 40000;
    boolean stop;

    abstract void sendMessage(ClientMessage msg);
    abstract void startListening(ClientController handler);
    public abstract boolean init();
    abstract void close();

    Client() {
        this.nPingLost  = 0;
        this.stop = true;
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
    void stopWaitingPing()
    {   //stop = true;
        if(disconnectionExecutor != null)
            disconnectionExecutor.shutdownNow();
    }


}
