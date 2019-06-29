package game.controller;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientMessage;
import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRMIClientHandler extends Remote {

    void sendMessage(ServerMessage msg) throws RemoteException;

    void receiveMessage(ClientMessage cmsg) throws RemoteException;

    void register(RemoteClient client) throws RemoteException;
}
