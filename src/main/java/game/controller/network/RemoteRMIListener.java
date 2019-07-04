package game.controller.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteRMIListener extends Remote {
    RemoteRMIClientHandler getHandler() throws RemoteException;
}
