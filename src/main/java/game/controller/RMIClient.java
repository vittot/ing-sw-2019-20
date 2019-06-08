package game.controller;

import game.controller.commands.ClientMessage;
import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;

import javax.sql.StatementEvent;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient implements Client {

    private transient ServerMessageHandler controller;

    @Override
    public void sendMessage(ClientMessage msg) {

    }

    @Override
    public void startListening(ServerMessageHandler handler) {
        this.controller = handler;
    }

    public void receiveMessage(ServerMessage msg) { ;
        msg.handle(controller);
    }

    @Override
    public void init() {
        try{
            Registry registry = LocateRegistry.getRegistry();

            for (String name : registry.list()) {
                System.out.println("Registry bindings: " + name);
            }
            System.out.println("\n");

            RMIListener remoteListener = (RMIListener) registry.lookup("rmiListener");

        }catch(RemoteException | NotBoundException e)
        {
            System.out.println("Remote EXCEPTION or NOT BOUND EXCEPTION");
        }

    }

    @Override
    public void close() {

    }
}
