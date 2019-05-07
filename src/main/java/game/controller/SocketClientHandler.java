package game.controller;

import game.controller.commands.ClientMessage;
import game.controller.commands.ServerMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.security.SecureRandom;

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


    /**
     * Receive clientMessage, send them to the controller to be handled and resend the answer to the Client
     */
    @Override
    public void run() {

        try {
            do {
                ClientMessage inMsg = (ClientMessage)inStream.readObject();
                ServerMessage outMsg = inMsg.handle(controller);
                outStream.writeObject(outMsg);
            } while (!stop);
            close();

        } catch (Exception e) {
            //TODO: handle exception
        }


    }

    /**
     * Set the server to be stopped
     */
    public void stop() {
        stop = true;
    }

    /**
     * Close the communication
     */
    private void close() throws IOException {
        stop = true;
        if (inStream != null)
            inStream.close();

        if (outStream != null)
            outStream.close();

        socket.close();
    }

}
