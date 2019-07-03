package game.controller;


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

public abstract class ClientHandler implements GameListener {

    protected ServerController controller;
    ScheduledExecutorService pingTimer;
    private ScheduledExecutorService periodicPingTimer;
    int nPingLost = 0;
    //static final int PING_WAITING_TIME_MS = 4000;
    //static final int PING_INTERVAL = 20000;
    boolean stop;
    protected String username;

    public abstract void sendMessage(ServerMessage msg);
    public abstract void sendPingMessage(PingMessage msg);

    ClientHandler()
    {
        stop = false;
    }

    void clientDisconnected()
    {
        /*try {
            pingTimer.cancel();
        }catch(IllegalStateException e){} //in case it has already been cancelled
        try{periodicPingTimer.cancel();}catch(IllegalStateException e){}*/
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
    void startPing(int pingPeriod)
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
                e.printStackTrace();
            }
        };
        //periodicPingTimer.scheduleAtFixedRate(task,0,pingPeriod);

        periodicPingTimer.scheduleAtFixedRate(task,0,pingPeriod, TimeUnit.MILLISECONDS);

    }

    /**
     * Interrupt the ping mechanism
     */
    void stopPing()
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

        this.controller.setState(ServerState.GAME_ENDED);
        sendMessage(new NotifyEndGame(ranking));
    }

    @Override
    public void onDamage(Player damaged, Player attacker, int damage, int marksToRemove) {
        sendMessage(new NotifyDamageResponse(attacker.getId(), damaged.getId(), damage, marksToRemove));
    }

    @Override
    public void onMarks(Player marked, Player marker, int marks) {
        sendMessage(new NotifyMarks(marker.getId(), marked.getId(), marks));
    }

    @Override
    public void onDeath(Kill kill) {
        sendMessage(new NotifyDeathResponse(kill));
    }

    @Override
    public void onGrabWeapon(Player p, CardWeapon cw, CardWeapon cww) {
        sendMessage(new NotifyGrabWeapon(p.getId(),cw,p.getPosition().getX(),p.getPosition().getY(),cww));
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
    public void onPlayerSuspend(Player p, boolean timeOut) {
        if(!controller.getCurrPlayer().equals(p)){
            sendMessage(new NotifyPlayerSuspend(p.getId()));
        }
        else if(timeOut)
            sendMessage(new TimeOutNotify());
    }

    @Override
    public void onPlayerRejoined(Player player) {
        if(!controller.getCurrPlayer().equals(player)){
            sendMessage(new NotifyPlayerRejoin(player.getId()));
        }
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
        sendMessage(new UpdateMarks(player.getId()));
    }

    @Override
    public void onPlayerRaged(Kill kill){
        sendMessage(new NotifyRage(kill));
    }

    @Override
    public void onFinalFrenzy() {
        sendMessage(new NotifyFinalFrenzy());
    }
}
