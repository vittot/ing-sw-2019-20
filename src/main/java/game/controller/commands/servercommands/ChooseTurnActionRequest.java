package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

/**
 * message to allow the choice of the turn action
 */
public class ChooseTurnActionRequest implements ServerGameMessage {

    /**
     * indicate if the movement is allowed for this turn
     */
    private boolean movAllowed;

    /**
     * construct the correct message
     * @param movAllowed
     */
    public ChooseTurnActionRequest(boolean movAllowed) {
        this.movAllowed = movAllowed;
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

    /**
     * return movAllowed attribute
     * @return movAllowed
     */
    public boolean isMovAllowed() {
        return movAllowed;
    }
}
