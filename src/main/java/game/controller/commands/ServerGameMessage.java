package game.controller.commands;

import java.io.Serializable;

/**
 * interface that describe the behavior for server messages about the game
 */
public interface ServerGameMessage extends ServerMessage {
    void handle(ServerGameMessageHandler handler) ;
}
