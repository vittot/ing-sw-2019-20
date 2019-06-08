package game.controller;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RMIListener extends UnicastRemoteObject {

    private List<RMIClientHandler> RMIHandlers;

    public RMIListener() throws RemoteException {
        super();
    }
}
