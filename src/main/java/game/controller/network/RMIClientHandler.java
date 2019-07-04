package game.controller.network;

import game.controller.Configuration;
import game.controller.GameManager;
import game.controller.ServerController;
import game.controller.commands.*;
import game.controller.commands.clientcommands.LoginMessage;
import game.controller.commands.clientcommands.PongMessage;
import game.controller.commands.servercommands.PingMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * RMI Network layer on server side
 */
public class RMIClientHandler extends ClientHandler implements RemoteRMIClientHandler, ClientMessageHandler {
    /**
     * Reference to the remote rmi client
     */
    private RemoteClient client;
    /**
     * Thread pool to send messages
     */
    private ExecutorService threadPool;

    /**
     * Default constructor
     * @param gm GameManager (need to be passed because otherwise rmi breaks the singleton pattern)
     * @throws RemoteException  in case of communication error
     */
    RMIClientHandler(GameManager gm) throws RemoteException {
        super();
        threadPool = Executors.newSingleThreadScheduledExecutor();
        //threadPool = Executors.newCachedThreadPool();
        this.controller = new ServerController(this,gm);
        UnicastRemoteObject.exportObject(this, 0);
    }

    /**
     * Send a message to the client
     * @param msg message
     */
    @Override
    public void sendMessage(ServerMessage msg) {

            final ServerController myController = controller;
            threadPool.submit(() ->
                    {
                            if(!stop){
                                if(myController.getCurrPlayer() != null)
                                    myController.getCurrPlayer().setSerializeEverything(true);
                                try {
                                    client.receiveMessage(msg);
                                }catch(RemoteException e)
                                {
                                    clientDisconnected();
                                }
                            }
                    }

            );

    }

    /**
     * Receive a message from the client
     * @param cmsg message
     * @throws RemoteException  in case of communication error
     */
    @Override
    public void receiveMessage(ClientMessage cmsg) throws RemoteException
    {
            cmsg.handle(this);
    }

    /**
     * Receive a pong answer from the client
     * @param msg pong answer
     * @throws RemoteException  in case of communication error
     */
    public void receivePongMessage(PongMessage msg) throws RemoteException
    {
        try {
            handle(msg);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Handle a clientGameMessage
     * @param msg message
     */
    @Override
    public void handle(ClientGameMessage msg) {
        ServerGameMessage answ = msg.handle(controller);
        sendMessage(answ);
    }

    /**
     * Handle a login message (just to save the username)
     * @param login
     */
    @Override
    public void handle(LoginMessage login) {
        this.username = login.getNickname();
        ServerGameMessage answ = login.handle(controller);
        sendMessage(answ);
    }

    /**
     * Handle a pong answer
     * @param msg pong answer
     */
    @Override
    public synchronized void handle(PongMessage msg) {
        pingTimer.shutdownNow();
        pingTimer = Executors.newScheduledThreadPool(1);
        System.out.println(username + " PONG");
        nPingLost = 0;
    }

    /**
     * Send a ping message
     * @param msg ping message
     */
    @Override
    public void sendPingMessage(PingMessage msg) {
        try {
            if(!stop)
            {
                System.out.println("PING " + username);
                client.receivePingMessage(msg);
            }
        } catch (RemoteException e) {
            clientDisconnected();
        }
    }

    /**
     * Register the client and starts ping
     * @param client rmi client
     * @throws RemoteException  in case of communication error
     */
    @Override
    public void register(RemoteClient client) throws RemoteException {
        this.client = client;
        startPing(Configuration.PING_INTERVAL_MS);
       // UnicastRemoteObject.unexportObject(this, false);
    }

}
