package game.controller;

import game.controller.commands.*;
import game.controller.commands.clientcommands.LoginMessage;
import game.controller.commands.clientcommands.PongMessage;
import game.controller.commands.servercommands.*;
import game.model.*;


import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.concurrent.Executors;


public class SocketClientHandler extends ClientHandler implements Runnable, ClientMessageHandler {

    private Socket socket;
    private final ObjectInputStream inStream;
    private final ObjectOutputStream outStream;
    private ObjectInputStream inPingStream;
    private ObjectOutputStream outPingStream;
    private Socket pingSocket;

    SocketClientHandler(Socket s) throws IOException {
        super();
        this.socket = s;
        this.outStream = new ObjectOutputStream(s.getOutputStream());
        this.inStream = new ObjectInputStream(s.getInputStream());
        this.controller = new ServerController(this);
    }


    /**
     * Send a message to the client
     * It must be synhcronized to avoid interferences between game messages and ping messages
     * @param msg
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
                e.printStackTrace();
            }
    }


    /**
     * Receive clientMessage, send them to the controller to be handled and resend the answer to the Client
     */
    @Override
    public void run() {

        startPing(PING_INTERVAL);
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
            //TODO: handle exception
            e.printStackTrace();
        }


    }

    public InetAddress getInetAddress()
    {
        return socket.getInetAddress();
    }

    public String getUsername() {
        return username;
    }


    @Override
    public void handle(ClientGameMessage inMsg) {
        //new Thread( () ->{
            ServerGameMessage outMsg = inMsg.handle(controller);
            sendMessage(outMsg);
        //}).start();

        /*if(controller.getCurrPlayer() != null)
            controller.getCurrPlayer().setSerializeEverything(false); //it is not always necessary*/
    }

    @Override
    public synchronized void handle(PongMessage msg) {
        //pingTimer.cancel();
        pingTimer.shutdownNow();
        nPingLost = 0;
        System.out.println(username + " PONG");
    }

    @Override
    public void handle(LoginMessage inMsg) {
        this.username = inMsg.getNickname();
        ServerGameMessage outMsg = inMsg.handle(controller);
        sendMessage(outMsg);
    }

    /**
     * Send a ping message
     * @param msg
     */
    @Override
    public void sendPingMessage(PingMessage msg) {

        System.out.println("PING " + username);
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
