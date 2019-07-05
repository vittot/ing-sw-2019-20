package game.controller.commands;

import game.controller.commands.clientcommands.LoginMessage;
import game.controller.commands.clientcommands.PongMessage;

/**
 * interface that describe the behavior to manage the reception of client messages
 */
public interface ClientMessageHandler {

    /**
     * manage the reception of client game messages
     * @param msg message
     */
    void handle(ClientGameMessage msg);

    /**
     * manage the reception of client pong messages (response to ping messages from the server)
     * @param msg message
     */
    void handle(PongMessage msg);

    /**
     * manage the reception of client login requests
     * @param login message
     */
    void handle(LoginMessage login);
}
