package game.controller;

import game.controller.commands.*;
import game.controller.commands.clientcommands.PongMessage;
import game.controller.commands.servercommands.PingMessage;
import game.model.Player;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RMIClientHandler extends ClientHandler implements IRMIClientHandler, ClientMessageHandler, Unreferenced {
    private transient RemoteClient client;
    private ExecutorService threadPool;

    RMIClientHandler(GameManager gm) throws RemoteException {
        super();
        //threadPool = Executors.newSingleThreadScheduledExecutor();
        threadPool = Executors.newCachedThreadPool();
        this.controller = new ServerController(this,gm);
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void sendMessage(ServerMessage msg) {

            final ServerController myController = controller;
            threadPool.submit(() ->
                    {
                            if(myController.getCurrPlayer() != null)
                                myController.getCurrPlayer().setSerializeEverything(true);
                            try {
                                client.receiveMessage(msg);
                            }catch(RemoteException e)
                            {
                                clientDisconnected();
                            }

                    }

            );

    }

    @Override
    public void receiveMessage(ClientMessage cmsg) throws RemoteException
    {
            cmsg.handle(this);
    }

    @Override
    public void handle(ClientGameMessage msg) {
        ServerGameMessage answ = msg.handle(controller);
        sendMessage(answ);
    }

    @Override
    public synchronized void handle(PongMessage msg) {
        pingTimer.cancel();
        nPingLost = 0;
    }

    @Override
    public void register(RemoteClient client) throws RemoteException {
        this.client = client;
        startPing(PING_INTERVAL);
       // UnicastRemoteObject.unexportObject(this, false);
    }

    @Override
    public void unreferenced() {
        clientDisconnected();
    }

    @Override
    public void onPlayerUpdateMarks(Player player) {

    }
}
