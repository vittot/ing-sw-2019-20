package game.controller.commands;

import game.controller.commands.servercommands.PingMessage;

/**
 * interface that describe the behavior to manage the reception of server messages
 */
public interface ServerMessageHandler {
    /**
     * manage the reception of server game messages
     * @param msg message
     */
    void handle(ServerGameMessage msg);

    /**
     * manage the reception of server ping messages
     * @param msg message
     */
    void handle(PingMessage msg);
}
