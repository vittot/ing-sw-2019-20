package game.controller;

import game.controller.commands.ClientMessage;
import game.controller.commands.ServerMessage;

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

    public void init() {
        try{
            socket = new Socket(host, port);
            outStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
        }catch(IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Send a message to the server
     * @param msg
     */
    public void sendMessage(ClientMessage msg) {
        try{
            outStream.writeObject(msg);
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    public ServerMessage receiveMessage() {
        try {
            return (ServerMessage) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return null;
    }

    public void close() {
        try {
            inputStream.close();
            outStream.close();
            socket.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    //TODO: send Requests and receive Responses
}
