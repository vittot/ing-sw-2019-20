package game.controller.network;

import game.controller.Configuration;
import game.controller.ServerController;
import game.controller.commands.*;
import game.controller.commands.clientcommands.LoginMessage;
import game.controller.commands.clientcommands.PongMessage;
import game.controller.commands.servercommands.*;
import game.controller.network.ClientHandler;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

/**
 * Network socket layer on server side
 */
public class SocketClientHandler extends ClientHandler implements Runnable, ClientMessageHandler {

    /** Client socket */
    private Socket socket;
    /** Client socket input stream*/
    private final ObjectInputStream inStream;
    /** Client socket ouput stream */
    private final ObjectOutputStream outStream;

    /**
     * Default constructor
     * @param s client socket
     * @throws IOException  in case of communication error
     */
    public SocketClientHandler(Socket s) throws IOException {
        super();
        this.socket = s;
        this.outStream = new ObjectOutputStream(s.getOutputStream());
        this.inStream = new ObjectInputStream(s.getInputStream());
        this.controller = new ServerController(this);
    }


    /**
     * Send a message to the client
     * It must be synhcronized to avoid interferences between game messages and ping messages
     * @param msg message
     */
    @Override
    public synchronized void sendMessage(ServerMessage msg) {
        //if(controller.getCurrPlayer() == null /*|| !controller.getCurrPlayer().isSuspended()*/)
            try{
                /*if(controller.getCurrPlayer() != null)
                    controller.getCurrPlayer().setSerializeEverything(true);*/
                if(!stop)
                {
                    outStream.writeObject(msg);
                    outStream.flush();
                    outStream.reset();
                }

                /*if(controller.getCurrPlayer() != null)
                    controller.getCurrPlayer().setSerializeEverything(false);*/
            }
            catch(SocketException | SocketTimeoutException e)
            {
                clientDisconnected();
            }
            catch(IOException e)
            {
                clientDisconnected();
                //e.printStackTrace();
            }
    }


    /**
     * Receive clientMessage, send them to the controller to be handled and resend the answer to the ClientNetwork
     */
    @Override
    public void run() {

        startPing(Configuration.PING_INTERVAL_MS);
        try {
            do {
                ClientMessage inMsg = (ClientMessage) inStream.readObject();
                inMsg.handle(this);
            } while (!stop);
            close();

        }
        catch(SocketException | EOFException e)
        {
            clientDisconnected();
        }
        catch (Exception e) {
            e.printStackTrace();
        }


    }

    /** Return the client username */
    @Override
    public String getUsername() {
        return username;
    }


    /**
     * Handle an incoming message from the client
     * @param inMsg message
     */
    @Override
    public void handle(ClientGameMessage inMsg) {
            ServerGameMessage outMsg = inMsg.handle(controller);
            sendMessage(outMsg);

    }

    /**
     * Handle a pong answer
     * @param msg pong answer
     */
    @Override
    public synchronized void handle(PongMessage msg) {
        pingTimer.shutdownNow();
        nPingLost = 0;
        //System.out.println(username + " PONG");
    }

    /**
     * Handle a login message (save the username)
     * @param inMsg message
     */
    @Override
    public void handle(LoginMessage inMsg) {
        this.username = inMsg.getNickname();
        ServerGameMessage outMsg = inMsg.handle(controller);
        sendMessage(outMsg);
    }

    /**
     * Send a ping message
     * @param msg ping message
     */
    @Override
    public void sendPingMessage(PingMessage msg) {

        //System.out.println("PING " + username);
        sendMessage(msg);
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
