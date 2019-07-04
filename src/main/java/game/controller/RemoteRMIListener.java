package game.controller;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteRMIListener extends Remote {
    RemoteRMIClientHandler getHandler() throws RemoteException;
}
