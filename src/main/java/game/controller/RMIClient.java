package game.controller;

import game.controller.commands.ClientMessage;
import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIClient extends UnicastRemoteObject implements Client, RemoteClient {

    private transient ServerMessageHandler controller;
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
            e.printStackTrace();
        }
    }

    @Override
    public void startListening(ClientController handler) {
        this.controller = handler;
    }

    @Override
    public synchronized void receiveMessage(ServerMessage msg) throws RemoteException {
        msg.handle(controller);
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
            e.printStackTrace();
            System.out.println("Remote EXCEPTION or NOT BOUND EXCEPTION");
            return false;
        }

    }

    @Override
    public void close() {

    }

}
