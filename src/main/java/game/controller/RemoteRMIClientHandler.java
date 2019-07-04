package game.controller;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientMessage;
import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerMessage;
import game.controller.commands.clientcommands.PongMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteRMIClientHandler extends Remote {

    void sendMessage(ServerMessage msg) throws RemoteException;

    void receiveMessage(ClientMessage cmsg) throws RemoteException;

    void receivePongMessage(PongMessage cmsg) throws RemoteException;

    void register(RemoteClient client) throws RemoteException;
}
