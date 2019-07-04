package game.controller.commands.servercommands;


import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;

/**
 * send a ping message to the client, to check if the connection is active
 */
public class PingMessage implements ServerMessage {
    /**
     * Handle the message
     * @param handler who handle the message
     */

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
