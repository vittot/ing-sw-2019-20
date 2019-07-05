package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardAmmo;
import game.model.Square;

/**
 * message that notify the card ammo refill on a square
 */
public class NotifyAmmoRefill implements ServerGameMessage {

    /**
     * card ammo added during the refill
     */
    private CardAmmo ca;
    /**
     * x coordinate of the considered square
     */
    private int x;

    /**
     * y coordinate of the considered square
     */
    private int y;

    /**
     * construct correct message
     * @param ca
     * @param position
     */
    public NotifyAmmoRefill(CardAmmo ca, Square position) {
        this.ca = ca;
        this.x = position.getX();
        this.y = position.getY();
    }

    /**
     * return the card ammo
     * @return ca
     */
    public CardAmmo getCa() {
        return ca;
    }

    /**
     * return x coordinate
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * return y coordinate
     * @return y
     */
    public int getY() {
        return y;
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
