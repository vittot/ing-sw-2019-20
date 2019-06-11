package game.controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRMIListener extends Remote {
    IRMIClientHandler getHandler() throws RemoteException;
}
