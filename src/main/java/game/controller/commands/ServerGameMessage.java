package game.controller.commands;

import java.io.Serializable;

/**
 * interface that describe the behavior for server messages about the game
 */
public interface ServerGameMessage extends ServerMessage {
    /**
     * Call the message handler
     * @param handler handler
     */
    void handle(ServerGameMessageHandler handler) ;
}
