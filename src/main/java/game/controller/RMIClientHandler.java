package game.controller;

import game.controller.commands.*;
import game.controller.commands.clientcommands.LoginMessage;
import game.controller.commands.clientcommands.PongMessage;
import game.controller.commands.servercommands.PingMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RMIClientHandler extends ClientHandler implements IRMIClientHandler, ClientMessageHandler {
    private RemoteClient client;
    private ExecutorService threadPool;

    RMIClientHandler(GameManager gm) throws RemoteException {
        super();
        threadPool = Executors.newSingleThreadScheduledExecutor();
        //threadPool = Executors.newCachedThreadPool();
        this.controller = new ServerController(this,gm);
        UnicastRemoteObject.exportObject(this, 0);
    }

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

    @Override
    public void receiveMessage(ClientMessage cmsg) throws RemoteException
    {
            cmsg.handle(this);
    }

    public void receivePongMessage(PongMessage msg) throws RemoteException
    {
        try {
            handle(msg);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void handle(ClientGameMessage msg) {
        ServerGameMessage answ = msg.handle(controller);
        sendMessage(answ);
    }

    @Override
    public void handle(LoginMessage login) {
        this.username = login.getNickname();
        ServerGameMessage answ = login.handle(controller);
        sendMessage(answ);
    }

    @Override
    public synchronized void handle(PongMessage msg) {
        pingTimer.shutdownNow();
        pingTimer = Executors.newScheduledThreadPool(1);
        System.out.println(username + " PONG");
        nPingLost = 0;
    }

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

    @Override
    public void register(RemoteClient client) throws RemoteException {
        this.client = client;
        startPing(Configuration.PING_INTERVAL_MS);
       // UnicastRemoteObject.unexportObject(this, false);
    }

}
