package game.controller.commands;


/**
 * interface that describe the behavior for client messages about the game
 */
public interface ClientGameMessage extends ClientMessage {
     /**
      * Call the message handler
      * @param handler message handler
      * @return server answer
      */
     ServerGameMessage handle(ClientGameMessageHandler handler);
}
