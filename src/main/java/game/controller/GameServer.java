package game.controller;

import game.controller.commands.clientcommands.LoginMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Manage incoming connections from clients
 */
public class GameServer {
    private ServerSocket serverSocket;
    private RMIListener rmiListener;
    private ExecutorService pool;
    private boolean close;
    private List<SocketClientHandler> socketClientHandlers;
    private static GameServer instance;

    private GameServer() {
        socketClientHandlers = new ArrayList<>();
        pool = Executors.newCachedThreadPool();
        close = false;
    }

    /**
     * Return the singleton instance of GameServer, creating it if it does not exist yet
     * @return
     */
    public static synchronized GameServer get() {
        if (instance == null) {
            instance = new GameServer();
        }

        return instance;
    }

    /**
     * Start listening on the given port with socket
     * Bind on RMI registry
     * @param port - port number for socket connections
     * @throws IOException
     */
    public void connect(int port) throws IOException
    {
        rmiListener = new RMIListener();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("rmiListener",rmiListener);
        System.out.println("GameServer bound on RMI registry");
        serverSocket = new ServerSocket(port);
        System.out.println("GameServer listening on port 5000");
    }

    /**
     * Manage socket connections
     * @throws IOException
     */
    public void run() throws IOException {
        Socket clientSocket;

        while (!close) {
            clientSocket = serverSocket.accept();
            SocketClientHandler h = new SocketClientHandler(clientSocket);
            socketClientHandlers.add(h);
            pool.submit(h);
        }
    }

    /**
     * Remove the clientHandler for a given username
     */
    public void removeHandler(String user, ClientHandler newHandler)
    {
        rmiListener.removeRMIClientHandler(user,newHandler);
        SocketClientHandler toRemove = null;
        for(SocketClientHandler h : socketClientHandlers)
            if(h.username.equals(user) && h!=newHandler)
            {
                h.stopPing();
                h.stop();
                toRemove = h;
            }
        this.socketClientHandlers.remove(toRemove);
    }

    /**
     * Stop listening connections
     * @throws IOException
     */
    public void close() throws IOException {
        close = true;
        serverSocket.close();
        pool.shutdown();
        //TODO unbind from registry for RMI

    }
}