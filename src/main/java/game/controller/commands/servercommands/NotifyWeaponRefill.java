package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardWeapon;
import game.model.Square;

/**
 * Notify the new weapon added to the map
 */
public class NotifyWeaponRefill implements ServerGameMessage {
    /**
     * Card weapon to add
     */
    private CardWeapon cw;
    /**
     * X position to add the card
     */
    private int x;
    /**
     * Y position to add the card
     */
    private int y;

    /**
     * Constructor
     * @param cw card weapon to add
     * @param position Square position in the map
     */
    public NotifyWeaponRefill(CardWeapon cw, Square position) {
        this.cw = cw;
        this.x = position.getX();
        this.y = position.getY();
    }

    /**
     *
     * @return weapon
     */
    public CardWeapon getCw() {
        return cw;
    }

    /**
     *
     * @return x position
     */
    public int getX() {
        return x;
    }

    /**
     *
     * @return y position
     */
    public int getY() {
        return y;
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
