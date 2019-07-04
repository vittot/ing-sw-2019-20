package game.controller.network;

import game.controller.commands.*;
import game.controller.commands.clientcommands.PongMessage;
import game.controller.commands.servercommands.PingMessage;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * RMI network layer on client side
 */
public class RMIClient extends ClientNetwork implements RemoteClient,ServerMessageHandler {

    /** Reference to the Remote Client Handler*/
    private RemoteRMIClientHandler rmiClientHandler;
    /** IP Address of the server*/
    private String serverIP;


    /**
     * Build the RMI Client network with the given server IP
     * @param serverIP server IP
     * @throws RemoteException in case of error
     */
    public RMIClient(String serverIP) throws RemoteException{
        super();
        this.serverIP = serverIP;

    }

    /**
     * Send a message to the server
     * @param msg message
     */
    @Override
    public void sendMessage(ClientMessage msg) {
        if(stop)
            return;
        try {
            rmiClientHandler.receiveMessage(msg);
        }
        catch(RemoteException e)
        {
            /*System.out.println("ECCEZIONE IN SEND MESSAGE");
            e.printStackTrace();*/
            controller.manageConnectionError();
        }
    }


    /**
     * Receive a message from the server
     * @param msg received message
     * @throws RemoteException in case of error
     */
    @Override
    public void receiveMessage(ServerMessage msg) throws RemoteException {
            msg.handle(this);
    }

    /**
     * Receive a ping message and send the pong answer
     * @param msg ping message
     * @throws RemoteException in case of error
     */
    @Override
    public void receivePingMessage(PingMessage msg) throws RemoteException {

        try{
            nPingLost = 0;
            if(disconnectionExecutor != null)
                disconnectionExecutor.shutdownNow();
            waitNextPing();
            rmiClientHandler.receivePongMessage(new PongMessage());
        }catch(Exception e)
        {
            controller.manageConnectionError();
        }

    }

    /**
     * Put the received message in the gameMessages queue
     * @param msg received message
     */
    @Override
    public synchronized void handle(ServerGameMessage msg) {
        //msg.handle(controller);
        try {
            gameMessages.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send the pong answer
     * @param msg ping message
     */
    @Override
    public void handle(PingMessage msg) {
        sendMessage(new PongMessage());
    }

    /**
     * Establish the remote connection, obtaining the RemoteClientHandler
     * @return true in case of success, false in case of error
     */
    @Override
    public boolean init() {
        if(!stop)
            close();
        try{
            Registry registry = LocateRegistry.getRegistry(serverIP);
            RemoteRMIListener remoteListener = (RemoteRMIListener) registry.lookup("rmiListener");
            UnicastRemoteObject.exportObject(this,0);
            this.rmiClientHandler = remoteListener.getHandler();
            this.rmiClientHandler.register(this);
            this.stop = false;
            return true;

        }catch(RemoteException | NotBoundException e)
        {
            return false;
        }

    }

    /**
     * Close the connection
     */
    @Override
    public void close() {
        stopWaitingPing();
        rmiClientHandler = null;
        try{
            UnicastRemoteObject.unexportObject(this,true);
        }catch (NoSuchObjectException e)
        {

        }
    }

}
