package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;

/**
 * Request of pick up the ammo in the player position
 */
public class PickUpAmmoRequest implements ClientGameMessage {

    /**
     * Handle the message
     * @param handler who handle the message
     * @return the message from the handler
     */
     @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */
    @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }
}
