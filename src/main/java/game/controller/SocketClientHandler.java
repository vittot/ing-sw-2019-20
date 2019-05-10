package game.controller;

import game.controller.commands.ClientMessage;
import game.controller.commands.ServerMessage;
import game.controller.commands.servercommands.NotifyEndGameResponse;
import game.model.GameListener;
import game.model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

public class SocketClientHandler implements Runnable, GameListener {

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
     * Send a message to the client
     * @param msg
     */
    public void sendMessage(ServerMessage msg) {
        try{
            outStream.writeObject(msg);
        }catch(IOException e)
        {
            e.printStackTrace();
        }
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
                sendMessage(outMsg);
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

    ///Listener methods

}
