package game.controller.commands;

import java.io.Serializable;

/**
 * interface that describe the behavior for client messages about the game
 */
public interface ClientGameMessage extends ClientMessage {
     ServerGameMessage handle(ClientGameMessageHandler handler);
}
