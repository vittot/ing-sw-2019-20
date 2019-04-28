package game.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient implements Client {
    private final String host;
    private final int port;
    private Socket socket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outStream;

    public SocketClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void init() throws IOException {
        socket = new Socket(host, port);
        outStream = new ObjectOutputStream(socket.getOutputStream());
        inputStream = new ObjectInputStream(socket.getInputStream());
    }

    public void close() throws IOException {
        inputStream.close();
        outStream.close();
        socket.close();
    }

    //TODO: send Requests and receive Responses
}
