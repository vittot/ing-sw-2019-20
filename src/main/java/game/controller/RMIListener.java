package game.controller;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

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


}
