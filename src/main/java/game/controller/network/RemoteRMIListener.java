package game.controller.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interfaced for the object published on the RMI registry by the server
 */
public interface RemoteRMIListener extends Remote {
    /**
     * Obtain a client handler for the rmi client (every client receive its own)
     * @return the RMIClientHandler
     * @throws RemoteException in case of communication error
     */
    RemoteRMIClientHandler getHandler() throws RemoteException;
}
