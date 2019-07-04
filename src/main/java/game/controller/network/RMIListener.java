package game.controller.network;

import game.controller.GameManager;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIListener extends UnicastRemoteObject implements RemoteRMIListener {

    private List<RMIClientHandler> rmiHandlers;

    private final transient GameManager gameManager;

    public RMIListener() throws RemoteException {
        super();
        rmiHandlers = new ArrayList<>();
        gameManager = GameManager.get();
    }

    public RemoteRMIClientHandler getHandler() throws RemoteException {
        RMIClientHandler h = new RMIClientHandler(gameManager);
        rmiHandlers.add(h);
        return h;
    }

    /**
     * Remove an RMIClientHandler
     * @param user username associated to the handler
     * @param newHandler
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
