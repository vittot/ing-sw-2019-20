package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

/**
 * error message
 */
public class InvalidMessageResponse implements ServerGameMessage {

    /**
     * message to show to the client
     */
    private String message;

    /**
     * construct correct message
     * @param message
     */
    public InvalidMessageResponse(String message) {
        this.message = message;
    }

    /**
     * Handle the message
     * @param handler who handle the message
     * @return the message from the handler
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the message
     * @return the message from the handler
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * return the string message
     * @return message
     */
    public String getMessage() {
        return message;
    }
}
