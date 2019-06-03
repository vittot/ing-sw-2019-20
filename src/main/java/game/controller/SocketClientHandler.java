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
import java.util.Map;
import java.util.SortedMap;
import java.util.SortedSet;

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
        stop = false;
        //the controller game will be set after the request of a new game or to join an existing game
    }


    /**
     * Send a message to the client
     * @param msg
     */
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

    ///Listener methods


    /**
     * Notify that it's the turn of another player
     * @param p
     */
    @Override
    public void onChangeTurn(Player p) {
        sendMessage(new NotifyTurnChanged(p.getId()));
        
    }

    /**
     * Notify that the game is finished and send the final game ranking
     * @param ranking - players ordered by points
     */
    @Override
    public void onGameEnd(SortedMap<Player,Integer> ranking) {

        sendMessage(new NotifyEndGame(ranking));
    }

    @Override
    public void onDamage(Player damaged, Player attacker, int damage) {
        sendMessage(new NotifyDamageResponse(attacker.getId(), damaged.getId(), damage));
    }

    @Override
    public void onMarks(Player marked, Player marker, int marks) {
        sendMessage(new NotifyDamageResponse(marker.getId(), marked.getId(), marks));
    }

    @Override
    public void onDeath(Player dead) {
        //TODO remove this or the next one
    }

    @Override
    public void onDeath(Kill kill) {
        sendMessage(new NotifyDeathResponse(kill));
    }

    @Override
    public void onGrabWeapon(Player p, CardWeapon cw) {
        sendMessage(new NotifyGrabWeapon(p.getId(),cw,p.getPosition().getX(),p.getPosition().getY()));
    }

    @Override
    public void onGrabCardAmmo(Player p, List<Color> ammo) {
        sendMessage(new NotifyGrabCardAmmo(p.getId(),p.getPosition().getX(),p.getPosition().getY(),ammo));
    }

    @Override
    public void onMove(Player p) {
        sendMessage(new NotifyMovement(p.getId(),p.getPosition().getX(),p.getPosition().getY()));
    }

    @Override
    public void onRespawn(Player p) {
        sendMessage(new NotifyRespawn(p.getId(),p.getPosition().getX(),p.getPosition().getY()));
    }

    @Override
    public void onPowerUpUse(Player p, CardPower c) {
        sendMessage(new NotifyPowerUpUsage(p.getId(),c));
    }

    @Override
    public void onPlayerSuspend(Player p) {
        if(!controller.getCurrPlayer().equals(p)){
            sendMessage(new NotifyPlayerSuspend(p.getId()));
        }
    }

    @Override
    public void onPlayerRejoined(Player player) {
        if(!controller.getCurrPlayer().equals(player)){
            sendMessage(new NotifyPlayerRejoin(player.getId()));
        }
    }
}
