package game.controller.commands;

import java.io.Serializable;

/**
 * interface that describe the behavior for all the kind of client messages
 */
public interface ClientMessage extends Serializable {
    /**
     * Call the handler
     * @param handler handler
     */
    void handle(ClientMessageHandler handler) ;
}
