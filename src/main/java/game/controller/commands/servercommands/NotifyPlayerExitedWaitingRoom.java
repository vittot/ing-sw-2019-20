package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

/**
 * notify that a player has quit the waiting room
 */
public class NotifyPlayerExitedWaitingRoom implements ServerGameMessage {

    /**
     * player id
     */
    private int playerId;

    /**
     * construct correct message
     * @param playerId
     */
    public NotifyPlayerExitedWaitingRoom(int playerId) {
        this.playerId = playerId;
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
