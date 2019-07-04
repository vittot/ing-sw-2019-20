package game.controller.network;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerMessage;
import game.controller.commands.servercommands.PingMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for RMI Client used on server side
 */
public interface RemoteClient extends Remote {

    /**
     * Receive a message from the server
     * @param msg message
     * @throws RemoteException in case of communication error
     */
    void receiveMessage(ServerMessage msg) throws RemoteException;

    /**
     * Receive a ping message from the server
     * @param msg ping message
     * @throws RemoteException in case of communication error
     */
    void receivePingMessage(PingMessage msg) throws RemoteException;

}
