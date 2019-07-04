package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.Kill;
import game.model.Player;

/**
 * Notify that a player got ragged
 */
public class NotifyRage implements ServerGameMessage {
    /**
     * Player that give rage
     */
    private Player killer;
    /**
     * Player that got ragged
     */
    private Player victim;

    /**
     * Constructor
     * @param kill Kill that has killer and victim
     */
    public NotifyRage(Kill kill) {
        this.killer = kill.getKiller();
        this.victim = kill.getVictim();
    }

    /**
     *
     * @return the player killed
     */
    public Player getKiller() {
        return killer;
    }

    /**
     *
     * @return the victim player
     */
    public Player getVictim() {
        return victim;
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
