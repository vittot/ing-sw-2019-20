package game.controller;


import game.controller.commands.ServerMessage;
import game.controller.commands.servercommands.*;
import game.model.*;
import game.model.exceptions.InsufficientAmmoException;

import java.util.List;
import java.util.SortedMap;

public abstract class ClientHandler implements GameListener {

    protected transient ServerController controller;

    public abstract void sendMessage(ServerMessage msg);

    protected void clientDisconnected()
    {
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
    public void onDamage(Player damaged, Player attacker, int damage) {
        sendMessage(new NotifyDamageResponse(attacker.getId(), damaged.getId(), damage));
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
}
