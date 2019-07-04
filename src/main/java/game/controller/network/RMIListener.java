package game.controller.network;

import game.controller.GameManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Object published on the RMI registry which manages all RMIClientHandlers
 */
public class RMIListener extends UnicastRemoteObject implements RemoteRMIListener {

    /**
     * List of all RMIClientHandlers
     */
    private List<RMIClientHandler> rmiHandlers;
    /** GameManager (needs to be passed because otherwise RMI breaks the singleton pattern */
    private final transient GameManager gameManager;

    /**
     * Default constructor
     * @throws RemoteException  in case of communication error
     */
    public RMIListener() throws RemoteException {
        super();
        rmiHandlers = new ArrayList<>();
        gameManager = GameManager.get();
    }

    /**
     * Return a new RMIClientHandler
     * @return rmiClientHandler
     * @throws RemoteException  in case of communication error
     */
    public RemoteRMIClientHandler getHandler() throws RemoteException {
        RMIClientHandler h = new RMIClientHandler(gameManager);
        rmiHandlers.add(h);
        return h;
    }

    /**
     * Remove an RMIClientHandler
     * @param user username associated to the handler
     * @param newHandler the new handler for this user (necessary to correctly remove the old)
     */
    public void removeRMIClientHandler(String user, ClientHandler newHandler)
    {
        RMIClientHandler toRemove = null;
        for(RMIClientHandler h : this.rmiHandlers)
            if(h.username.equals(user) && h != newHandler)
            {
                h.stopPing();
                h.stop();
                toRemove = h;
            }

        this.rmiHandlers.remove(toRemove);
    }


}
