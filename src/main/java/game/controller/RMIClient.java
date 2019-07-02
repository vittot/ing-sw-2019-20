package game.controller;

import game.controller.commands.*;
import game.controller.commands.clientcommands.PongMessage;
import game.controller.commands.servercommands.PingMessage;

import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.*;

public class RMIClient extends Client implements RemoteClient,ServerMessageHandler {

    private transient IRMIClientHandler rmiClientHandler;
    private String serverIP;
    private LinkedBlockingQueue<ServerGameMessage> gameMessages;
    private Thread processer;


    public RMIClient(String serverIP) throws RemoteException{
        super();
        this.serverIP = serverIP;
        this.nPingLost = 0;
        this.gameMessages = new LinkedBlockingQueue<>();
    }

    @Override
    public void sendMessage(ClientMessage msg) {
        if(stop)
            return;
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
        this.disconnectionExecutor = Executors.newSingleThreadScheduledExecutor();
        processer = new Thread(
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
        processer.setName("PROCESSER THREAD");
        processer.start();
        waitNextPing();
    }


    @Override
    public void receiveMessage(ServerMessage msg) throws RemoteException {
            msg.handle(this);
    }

    @Override
    public void receivePingMessage(PingMessage msg) throws RemoteException {
        //System.out.println("PING FROM SERVER");
        try{
            nPingLost = 0;
            if(disconnectionExecutor != null)
                disconnectionExecutor.shutdownNow();
            waitNextPing();
            rmiClientHandler.receivePongMessage(new PongMessage());
        }catch(Exception e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public synchronized void handle(ServerGameMessage msg) {
        //msg.handle(controller);
        try {
            gameMessages.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(PingMessage msg) {
        sendMessage(new PongMessage());
    }

    @Override
    public boolean init() {
        if(!stop)
            close();
        try{
            Registry registry = LocateRegistry.getRegistry(serverIP);
            IRMIListener remoteListener = (IRMIListener) registry.lookup("rmiListener");
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
