package game.controller.network;


import game.controller.Configuration;
import game.controller.GameManager;
import game.controller.ServerController;
import game.controller.ServerState;
import game.controller.commands.ServerMessage;
import game.controller.commands.servercommands.*;
import game.model.*;

import java.util.List;
import java.util.SortedMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Network layer on server side
 */
public abstract class ClientHandler implements GameListener {

    /** Reference to the controller  */
    protected ServerController controller;
    /** Ping timer */
    ScheduledExecutorService pingTimer;
    /** Periodic ping schduler */
    private ScheduledExecutorService periodicPingTimer;
    /** Number of ping lost*/
    int nPingLost = 0;
    //static final int PING_WAITING_TIME_MS = 4000;
    //static final int PING_INTERVAL = 20000;
    /** True when the communication has to be stopped*/
    boolean stop;
    /** Client username */
    protected String username;

    /**
     * Send a message to the client
     * @param msg message
     */
    public abstract void sendMessage(ServerMessage msg);

    /**
     * Send a ping message to the client
     * @param msg ping message
     */
    public abstract void sendPingMessage(PingMessage msg);

    /**
     * Deault constructor
     */
    ClientHandler()
    {
        stop = false;
    }

    /**
     * Communicate the client disconnection to controller and model
     */
    void clientDisconnected()
    {

        stopPing();
        stop();

        if(controller.getState() != ServerState.WAITING_FOR_PLAYERS && controller.getState() != ServerState.JUST_LOGGED && controller.getState() != ServerState.GAME_ENDED){
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

    /**
     * Start the ping-pong system
     * @param pingPeriod period between two ping requests, in ms
     */
    public void startPing(int pingPeriod)
    {
        if(periodicPingTimer != null)
            periodicPingTimer.shutdownNow();
        if(pingTimer != null)
            pingTimer.shutdownNow();
        periodicPingTimer = Executors.newSingleThreadScheduledExecutor();
        pingTimer = Executors.newScheduledThreadPool(1);
        Runnable task = ()-> {
            try {
                pingClient(Configuration.PING_WAITING_TIME_MS);
            }catch(Exception e){
                //e.printStackTrace();
            }
        };

        periodicPingTimer.scheduleAtFixedRate(task,0,pingPeriod, TimeUnit.MILLISECONDS);

    }

    /**
     * Interrupt the ping mechanism
     */
    public void stopPing()
    {
        pingTimer.shutdownNow();
        periodicPingTimer.shutdownNow();
    }

    /**
     * Send a ping message to the client
     * If it does not receive an answer within waitingTime, it increments the number of lost pings.
     * When the number of lost pings arrives at 3 it considers the client disconnected
     * @param waitingTime in ms
     */
    void pingClient(int waitingTime)
    {
        //pingTimer = new Timer();
        sendPingMessage(new PingMessage());
        Runnable task = () -> {
                nPingLost++;
                if(nPingLost >= 3){
                    System.out.println("HO DISCONNESSO PER PERDITA 3 PING");
                    clientDisconnected();

                }
            };

        //pingTimer.schedule(task, waitingTime);
        synchronized (this){
            pingTimer = Executors.newScheduledThreadPool(1);
            pingTimer.schedule(task,waitingTime,TimeUnit.MILLISECONDS);
        }

    }

    /**
     * Return the username
     * @return
     */
    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Stop the client handler
     */
    void stop()
    {
        stop = true;
    }

    /**
     * Notify that it's the turn of another player
     * @param p player of the new turn
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

        this.controller.setState(ServerState.GAME_ENDED);
        sendMessage(new NotifyEndGame(ranking));
    }

    /**
     * Notify about a damage occurred
     * @param damaged damaged player
     * @param attacker attacker player
     * @param damage damage amount
     * @param marksToRemove marks to be removed from damaged player
     */
    @Override
    public void onDamage(Player damaged, Player attacker, int damage, int marksToRemove) {
        sendMessage(new NotifyDamageResponse(attacker.getId(), damaged.getId(), damage, marksToRemove));
    }

    /**
     * Notify about marks given to a player
     * @param marked marked player
     * @param marker marker player
     * @param marks marks amount
     */
    @Override
    public void onMarks(Player marked, Player marker, int marks) {
        sendMessage(new NotifyMarks(marker.getId(), marked.getId(), marks));
    }

    /**
     * Notify about a death
     * @param kill the kill
     */
    @Override
    public void onDeath(Kill kill) {
        sendMessage(new NotifyDeathResponse(kill));
    }

    /**
     * Notify about a weapon grab
     * @param p player who grab
     * @param cw weapon grabbed
     * @param cww weapon eventually leaved by the player
     */
    @Override
    public void onGrabWeapon(Player p, CardWeapon cw, CardWeapon cww) {
        sendMessage(new NotifyGrabWeapon(p.getId(),cw,p.getPosition().getX(),p.getPosition().getY(),cww));
    }

    /**
     * Notify about a card ammo grab
     * @param p player who grab
     * @param ammo ammos grabbed
     */
    @Override
    public void onGrabCardAmmo(Player p, List<Color> ammo) {
        sendMessage(new NotifyGrabCardAmmo(p.getId(),p.getPosition().getX(),p.getPosition().getY(),ammo));
    }

    /**
     * Notify about a movement
     * @param p player moved
     */
    @Override
    public void onMove(Player p) {
        sendMessage(new NotifyMovement(p.getId(),p.getPosition().getX(),p.getPosition().getY()));
    }

    /**
     * Notify about a respawn
     * @param p
     */
    @Override
    public void onRespawn(Player p) {
        sendMessage(new NotifyRespawn(p.getId(),p.getPosition().getX(),p.getPosition().getY()));
    }

    /**
     * Notify about the use of a power up
     * @param p player who used the power up
     * @param c power up used
     */
    @Override
    public void onPowerUpUse(Player p, CardPower c) {
        sendMessage(new NotifyPowerUpUsage(p,c));
    }

    /**
     * Notify about a player suspension
     * @param p player suspended
     * @param timeOut true if the suspension is due to turn timeout
     */
    @Override
    public void onPlayerSuspend(Player p, boolean timeOut) {
        if(!controller.getCurrPlayer().equals(p)){
            sendMessage(new NotifyPlayerSuspend(p.getId()));
        }
        else if(timeOut)
            sendMessage(new TimeOutNotify());
    }

    /**
     * Notify about a player rejoin
     * @param player player rejoined
     */
    @Override
    public void onPlayerRejoined(Player player) {
        if(!controller.getCurrPlayer().equals(player)){
            sendMessage(new NotifyPlayerRejoin(player.getId()));
        }
    }

    /**
     * Notify about a weapon refill
     * @param cw weapon refilled
     * @param s position
     */
    @Override
    public void onReplaceWeapon(CardWeapon cw, Square s) {
        sendMessage(new NotifyWeaponRefill(cw,s));
    }

    /**
     * Notify about a card ammo refill
     * @param ca card ammo refilled
     * @param s position
     */
    @Override
    public void onReplaceAmmo(CardAmmo ca, Square s) {
        sendMessage(new NotifyAmmoRefill(ca,s));
    }

    /**
     * Notify on the update of player marks
     * @param player player
     */
    @Override
    public void onPlayerUpdateMarks(Player player) {
        sendMessage(new UpdateMarks(player.getId()));
    }

    /**
     * Notify about player raged
     * @param kill kill
     */
    @Override
    public void onPlayerRaged(Kill kill){
        sendMessage(new NotifyRage(kill));
    }

    /**
     * Notify about final frenzy start
     */
    @Override
    public void onFinalFrenzy() {
        sendMessage(new NotifyFinalFrenzy());
    }
}
