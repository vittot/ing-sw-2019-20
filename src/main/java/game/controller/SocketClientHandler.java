package game.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClientHandler implements Runnable {

    private Socket socket;
    private final ObjectInputStream inStream;
    private final ObjectOutputStream outStream;
    private boolean stop;

    private ServerController controller;

    public SocketClientHandler(Socket s) throws IOException {
        this.socket = s;
        this.outStream = new ObjectOutputStream(s.getOutputStream());
        this.inStream = new ObjectInputStream(s.getInputStream());

        this.controller = new ServerController(this);
        //the controller game will be set after the request of a new game or to join an existing game
    }


    @Override
    public void run() {
        //TODO: receive Requests and send them to the ServerController
    }

}
