package game.controller.network;

import game.controller.commands.ClientMessage;
import game.controller.commands.ServerMessage;
import game.controller.commands.clientcommands.PongMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for RMI server object used on client side
 */
public interface RemoteRMIClientHandler extends Remote {

    /**
     * Send a message to the client
     * @param msg message
     * @throws RemoteException in case of communication error
     */
    void sendMessage(ServerMessage msg) throws RemoteException;

    /**
     * Receive a message from the client
     * @param cmsg message
     * @throws RemoteException in case of communication error
     */
    void receiveMessage(ClientMessage cmsg) throws RemoteException;

    /**
     * Receive a pong response from the client
     * @param cmsg pong message
     * @throws RemoteException in case of communication error
     */
    void receivePongMessage(PongMessage cmsg) throws RemoteException;

    /**
     * Register the rmi client and starts to ping it
     * @param client rmi client
     * @throws RemoteException  in case of communication error
     */
    void register(RemoteClient client) throws RemoteException;
}
