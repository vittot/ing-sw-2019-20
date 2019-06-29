package game.controller;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteClient extends Remote {

    void receiveMessage(ServerMessage msg) throws RemoteException;


}
