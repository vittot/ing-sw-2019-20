package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.Color;

import java.util.List;

/**
 * notify the occurred grab of a card ammo
 */
public class NotifyGrabCardAmmo implements ServerGameMessage {

    /**
     * id of the player that complete the grab action
     */
    private int pId;
    /**
     * x coordinate of the square where the player grab ammos
     */
    private int x;
    /**
     * y coordinate of the square where the player grab ammos
     */
    private int y;
    /**
     * list of ammos grabbed by the player
     */
    private List<Color> ammos;
    //private List<CardPower> powerUps;

    /**
     * construct correct message
     * @param pId
     * @param x
     * @param y
     * @param ammos
     */
    public NotifyGrabCardAmmo(int pId, int x, int y, List<Color> ammos) {
        this.pId = pId;
        this.x = x;
        this.y = y;
        this.ammos = ammos;
    }

    /**
     * return the player id
     * @return id
     */
    public int getpId() {
        return pId;
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
     * return list of ammos
     * @return ammos
     */
    public List<Color> getAmmos() {
        return ammos;
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
