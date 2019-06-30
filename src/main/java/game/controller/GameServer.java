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

    /**
     *
     * @param port Listening port number for client connections
     * @throws IOException
     */
    public GameServer(int port) throws IOException {
        socketClientHandlers = new ArrayList<>();
        serverSocket = new ServerSocket(port);
        pool = Executors.newCachedThreadPool();
        close = false;
        System.out.println("GameServer listening on port 5000");
        //TODO bind on registy for RMI
        rmiListener = new RMIListener();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.rebind("rmiListener",rmiListener);
    }

    /**
     * Manage socket connections
     * @throws IOException
     */
    public void run() throws IOException {
        Socket clientSocket;

        /*new Thread( ()->{
            while(!close)
            {
                Socket clientPingSocket;
                LoginMessage msg;
                ObjectInputStream ois;
                try {
                    clientPingSocket = pingSocket.accept();
                    ois = new ObjectInputStream(clientPingSocket.getInputStream());
                    msg = (LoginMessage)ois.readObject();
                    for (SocketClientHandler h : socketClientHandlers) {
                        if (h.getUsername().equals(msg.getNickname()))
                            h.setPingSocket(clientPingSocket);
                    }
                }catch(IOException | ClassNotFoundException e){
                    e.printStackTrace();
                }
            }
        }).start();*/


        while (!close) {
            clientSocket = serverSocket.accept();
            SocketClientHandler h = new SocketClientHandler(clientSocket);
            socketClientHandlers.add(h);
            pool.submit(h);
        }
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