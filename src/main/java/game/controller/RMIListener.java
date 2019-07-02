package game.controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.RMIClassLoader;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RMIListener extends UnicastRemoteObject implements IRMIListener {

    private List<RMIClientHandler> rmiHandlers;

    private final transient GameManager gameManager;

    RMIListener() throws RemoteException {
        super();
        rmiHandlers = new ArrayList<>();
        gameManager = GameManager.get();
    }

    public IRMIClientHandler getHandler() throws RemoteException {
        RMIClientHandler h = new RMIClientHandler(gameManager);
        rmiHandlers.add(h);
        return h;
    }

    /**
     * Remove an RMIClientHandler
     * @param user username associated to the handler
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
