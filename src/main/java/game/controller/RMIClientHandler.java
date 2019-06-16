package game.controller;

import game.controller.commands.ClientMessage;
import game.controller.commands.ServerMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.server.Unreferenced;

public class RMIClientHandler extends ClientHandler implements IRMIClientHandler, Unreferenced {
    private transient RemoteClient client;

    RMIClientHandler(GameManager gm) throws RemoteException {
        super();
        this.controller = new ServerController(this,gm);
        UnicastRemoteObject.exportObject(this, 0);
    }

    @Override
    public void sendMessage(ServerMessage msg) {
        try{
            client.receiveMessage(msg);
        }catch (RemoteException e)
        {
            e.printStackTrace();
            clientDisconnected();
        }
    }

    @Override
    public void receiveMessage(ClientMessage cmsg) throws RemoteException
    {
        ServerMessage answ = cmsg.handle(controller);
        sendMessage(answ);
    }

    @Override
    public void register(RemoteClient client) throws RemoteException {
        this.client = client;
       // UnicastRemoteObject.unexportObject(this, false);
    }

    @Override
    public void unreferenced() {
        clientDisconnected();
    }

}
