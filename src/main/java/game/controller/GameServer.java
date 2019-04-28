package game.controller;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Manage incoming connections from clients
 */
public class GameServer {
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private boolean close;

    /**
     *
     * @param port Listening port number for client connections
     * @throws IOException
     */
    public GameServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        pool = Executors.newCachedThreadPool();
        close = false;
        //TODO bind on registyr for RMI
    }

    /**
     * Manage socket connections
     * @throws IOException
     */
    public void run() throws IOException {
        Socket clientSocket;
        while (!close) {
            clientSocket = serverSocket.accept();
            pool.submit(new SocketClientHandler(clientSocket));
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