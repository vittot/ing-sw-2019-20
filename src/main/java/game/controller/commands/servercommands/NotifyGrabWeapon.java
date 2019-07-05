package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardWeapon;

/**
 * notify the occurred grab of a new weapon and if necessary the weapon to waste to complete the grab
 */
public class NotifyGrabWeapon implements ServerGameMessage {
    /**
     id of the player that grab the weapon
     */
    private int p;
    /**
     * weapon grabbed
     */
    private CardWeapon cw;
    /**
     * x coordinate of the square where the player grab the weapon
     */
    private int x;
    /**
     * y coordinate of the square where the player grab ammos
     */
    private int y;
    /**
     * if necessary, reference to the weapon to waste
     */
    private CardWeapon wWaste;

    /**
     * construct correct message
     * @param p
     * @param cw
     * @param x
     * @param y
     * @param wWaste
     */
    public NotifyGrabWeapon(int p, CardWeapon cw, int x, int y, CardWeapon wWaste) {
        this.p = p;
        this.cw = cw;
        this.x = x;
        this.y = y;
        this.wWaste = wWaste;
    }

    /**
     * return weapon to waste
     * @return wWaste
     */
    public CardWeapon getwWaste() {
        return wWaste;
    }

    /**
     * return player id
     * @return p
     */
    public int getP() {
        return p;
    }

    /**
     * return weapon to grab
     * @return cw
     */
    public CardWeapon getCw() {
        return cw;
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