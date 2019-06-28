package game.controller;

import game.controller.commands.*;
import game.controller.commands.clientcommands.PongMessage;
import game.controller.commands.servercommands.*;
import game.model.*;


import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;


public class SocketClientHandler extends ClientHandler implements Runnable, ClientMessageHandler {

    private Socket socket;
    private final ObjectInputStream inStream;
    private final ObjectOutputStream outStream;
    private boolean stop;

    SocketClientHandler(Socket s) throws IOException {
        this.socket = s;
        this.outStream = new ObjectOutputStream(s.getOutputStream());
        this.inStream = new ObjectInputStream(s.getInputStream());

        this.controller = new ServerController(this);
        stop = false;
        //the controller game will be set after the request of a new game or to join an existing game
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
                if(controller.getCurrPlayer() != null)
                    controller.getCurrPlayer().setSerializeEverything(true);
                outStream.writeObject(msg);
                outStream.reset();
                /*if(controller.getCurrPlayer() != null)
                    controller.getCurrPlayer().setSerializeEverything(false);*/
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

    @Override
    public void handle(ClientGameMessage inMsg) {
        ServerGameMessage outMsg = inMsg.handle(controller);
        sendMessage(outMsg);
        /*if(controller.getCurrPlayer() != null)
            controller.getCurrPlayer().setSerializeEverything(false); //it is not always necessary*/
    }

    @Override
    public synchronized void handle(PongMessage msg) {
        pingTimer.cancel();
        nPingLost = 0;
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


    @Override
    public void onReplaceWeapon(CardWeapon cw, Square s) {
        sendMessage(new NotifyWeaponRefill(cw,s));
    }

    @Override
    public void onReplaceAmmo(CardAmmo ca, Square s) {
        sendMessage(new NotifyAmmoRefill(ca,s));
    }

    @Override
    public void onPlayerUpdateMarks(Player player) {
        sendMessage(new UpdateMarks(player));
    }


}
