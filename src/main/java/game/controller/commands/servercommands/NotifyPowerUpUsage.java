package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;
import game.model.Player;

/**
 * notify the correct usage of power-up card
 */
public class NotifyPowerUpUsage implements ServerGameMessage {
    private Player p; /** reference to the player that used the power-up card effect */
    private CardPower cp; /** power-up card used */

    /**
     * construct the message
     * @param p
     * @param cp
     */
    public NotifyPowerUpUsage(Player p, CardPower cp) {
        this.p = p;
        this.cp = cp;
    }

    /**
     * return the player
     * @return p
     */
    public Player getP() {
        return p;
    }

    /**
     * return the power-up card
     * @return cp
     */
    public CardPower getCp() {
        return cp;
    }

    /**
     * Handle the message
     * @param handler who handle the messag
     * @param handler
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the messag
     * @param handler
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}