package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

/**
 * Notify the changed of the turn and the new current player
 */
public class NotifyTurnChanged implements ServerGameMessage {
    /**
     * Id of the new player in action
     */
    private int currPlayerId;

    /**
     * Constructor
     * @param currPlayerId id of the player
     */
    public NotifyTurnChanged(int currPlayerId) {
        this.currPlayerId = currPlayerId;
    }

    /**
     *
     * @return the current player id
     */
    public int getCurrPlayerId() {
        return currPlayerId;
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
