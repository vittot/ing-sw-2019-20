package game.controller;

import game.controller.commands.*;
import game.controller.commands.clientcommands.PongMessage;
import game.controller.commands.servercommands.PingMessage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient extends UnicastRemoteObject implements Client, RemoteClient,ServerMessageHandler {

    private transient ServerGameMessageHandler controller;
    private transient IRMIClientHandler rmiClientHandler;
    private String serverIP;

    public RMIClient(String serverIP) throws RemoteException{
        super();
        this.serverIP = serverIP;
    }

    @Override
    public void sendMessage(ClientMessage msg) {
        try {
            rmiClientHandler.receiveMessage(msg);
        }
        catch(RemoteException e)
        {
            System.out.println("ECCEZIONE IN SEND MESSAGE"); //TODO: call retry connection method
            e.printStackTrace();
        }
    }

    @Override
    public void startListening(ClientController handler) {
        this.controller = handler;
    }

    @Override
    public void receiveMessage(ServerMessage msg) throws RemoteException {
        msg.handle(this);
    }

    @Override
    public synchronized void handle(ServerGameMessage msg) {
        msg.handle(controller);
    }

    @Override
    public void handle(PingMessage msg) {
        sendMessage(new PongMessage());
    }

    @Override
    public boolean init() {
        try{
            Registry registry = LocateRegistry.getRegistry(serverIP);

            IRMIListener remoteListener = (IRMIListener) registry.lookup("rmiListener");
            this.rmiClientHandler = remoteListener.getHandler();
            this.rmiClientHandler.register(this);
            return true;

        }catch(RemoteException | NotBoundException e)
        {
            return false;
        }

    }

    @Override
    public void close() {
        rmiClientHandler = null;
        System.exit(0); //necessary to close the rmi background thread
    }

}
