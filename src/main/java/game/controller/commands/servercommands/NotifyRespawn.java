package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

/**
 * Notify that a player respawned in the amp
 */
public class NotifyRespawn implements ServerGameMessage {
    /**
     * Id of the player respawned
     */
    private int pId;
    /**
     * X position
     */
    private int x;
    /**
     * Y position
     */
    private int y;

    /**
     * Constructor
     * @param pId int
     * @param x int
     * @param y int
     */
    public NotifyRespawn(int pId, int x, int y) {
        this.pId = pId;
        this.x = x;
        this.y = y;
    }

    /**
     *
     * @return player id
     */
    public int getpId() {
        return pId;
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
     * @param handler who handle the messag
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the messag
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }


}
