package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

/**
 * notify that a player was suspended from the game
 */
public class NotifyPlayerSuspend implements ServerGameMessage {

    private int pId; /** player id */

    /**
     * construct correct message
     * @param id
     */
    public NotifyPlayerSuspend(int id) {
        this.pId = id;
    }

    /**
     * return the player id
     * @return pId
     */
    public int getpId() {
        return pId;
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
