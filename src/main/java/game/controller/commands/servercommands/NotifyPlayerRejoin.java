package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

/**
 * notify that a player rejoined the the game
 */
public class NotifyPlayerRejoin implements ServerGameMessage {

    private int playerId; /** player id */

    /**
     * construct correct message
     * @param id
     */
    public NotifyPlayerRejoin(int id) {
        this.playerId = id;
    }

    /**
     * return player id
     * @return playerId
     */
    public int getPlayerId() {
        return playerId;
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
