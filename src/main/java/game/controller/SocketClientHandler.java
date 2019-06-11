package game.controller;

import game.controller.commands.ClientMessage;
import game.controller.commands.ServerMessage;
import game.controller.commands.servercommands.*;
import game.model.*;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;


public class SocketClientHandler extends ClientHandler implements Runnable {

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
     * @param msg
     */
    @Override
    public void sendMessage(ServerMessage msg) {
        if(controller.getCurrPlayer() == null || !controller.getCurrPlayer().isSuspended())
            try{
                outStream.writeObject(msg);
                outStream.reset();
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
                if(controller.getCurrPlayer() != null)
                    controller.getCurrPlayer().setSerializeEverything(false); //it is not always necessary
            } while (!stop);
            close();

        }
        catch(SocketException e)
        {
            if(controller.getState() != ServerState.WAITING_FOR_PLAYERS && controller.getState() != ServerState.JUST_LOGGED){
                controller.getModel().removeGameListener(this);
                controller.getCurrPlayer().suspend(false);
            }
            else if(controller.getState() == ServerState.WAITING_FOR_PLAYERS)
            {
                controller.leaveWaitingRoom();
            }
            else
            {
                GameManager.get().removeLoggedUser(controller.getNickname());
            }
        }
        catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
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


    @Override
    public void onReplaceWeapon(CardWeapon cw, Square s) {
        sendMessage(new NotifyWeaponRefill(cw,s));
    }

    @Override
    public void onReplaceAmmo(CardAmmo ca, Square s) {
        sendMessage(new NotifyAmmoRefill(ca,s));
    }

}
