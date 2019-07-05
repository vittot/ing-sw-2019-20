package game.controller.commands;

import java.io.Serializable;

/**
 * interface that describe the behavior for all the kind of server messages
 */
public interface ServerMessage extends Serializable {
    /**
     * Call the handler
     * @param handler handler
     */
    void handle(ServerMessageHandler handler) ;
}
