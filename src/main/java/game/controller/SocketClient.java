package game.controller;

import game.controller.commands.*;
import game.controller.commands.clientcommands.PongMessage;
import game.controller.commands.servercommands.PingMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * Socket network layer on client side
 */
public class SocketClient extends ClientNetwork implements ServerMessageHandler {
    /** Server ip */
    private final String host;
    /** Server port */
    private final int port;
    /** Socket object */
    private Socket socket;
    /** Thread listening for incoming messages */
    private Thread receiver;
    /** Socket inputStream */
    private ObjectInputStream inputStream;
    /** Socket outputStream */
    private ObjectOutputStream outStream;

    /**
     * Build the network layer for the given ip:port server
     * @param host server ip
     * @param port server port
     */
    public SocketClient(String host, int port) {
        super();
        this.host = host;
        this.port = port;
    }

    /**
     * Open the connection
     * @return true in case of success, false in case of error
     */
    public boolean init() {
        try{
            if(socket != null && socket.isConnected())
                close();
            socket = new Socket(host, port);
            outStream = new ObjectOutputStream(socket.getOutputStream());
            inputStream = new ObjectInputStream(socket.getInputStream());
            this.stop = false;
            return true;
        }
        catch(IOException e)
        {
            return false;
        }

    }

    /**
     * Start processor and receiver threads
     * @param handler the ClientController
     */
    @Override
    public void startListening(ClientController handler)
    {

        super.startListening(handler);

        receiver = new Thread(

                () -> {
                    ServerMessage sm;
                    do{
                        sm = this.receiveMessage();
                        if(sm!=null)
                        {
                            final ServerMessage smR = sm;
                            smR.handle(this);
                        }

                    }while(sm!=null && !stop);
                    if(!stop)
                    {
                        //System.out.println("ERROR received : " + sm);
                        handler.manageConnectionError();
                    }
                }
        );
        receiver.setName("RECEIVER THREAD");
        receiver.start();


    }

    /**
     * Send a message to the server
     * It must be synchronized to avoid interferences between game messages and ping messages
     * @param msg message
     */
    public synchronized void sendMessage(ClientMessage msg) {
        if(stop)
            return;
        try{
            outStream.writeObject(msg);
            outStream.flush();
            outStream.reset();
        }catch(IOException e)
        {
            /*System.out.println("ECCEZIONE IN SEND MESSAGE");
            e.printStackTrace();*/

            controller.manageConnectionError();

        }
    }

    /**
     * Receive a message from the server
     * @return received message
     */
    private ServerMessage receiveMessage() {
        try {
            return (ServerMessage) inputStream.readObject();
        } catch (IOException e){
            /*System.out.println("IOEXCEPTION READING");
            e.printStackTrace();*/
            return null;
        }catch(ClassNotFoundException e)
        {
            /*System.out.println("CLASS NOT FOUND EXCEPTION READING");
            e.printStackTrace();*/
            return null;
        }
    }

    /**
     * Put a receive message in the gameMessages queue
     * @param msg received message
     */
    @Override
    public void handle(ServerGameMessage msg) {
        try {
            gameMessages.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Send pong answer for a received ping
     * @param msg received ping
     */
    @Override
    public void handle(PingMessage msg) {
        sendMessage(new PongMessage());
        //System.out.println("PING FROM SERVER");
        nPingLost = 0;
        disconnectionExecutor.shutdownNow();
        waitNextPing();
    }

    /**
     * Close the connection
     */
    public void close() {
        try {
            stop = true;
            stopWaitingPing();
            inputStream.close();
            outStream.close();
            socket.close();
            receiver.interrupt();
            processor.interrupt();
        }catch(IOException e)
        {
            //e.printStackTrace();
        }
    }
}
