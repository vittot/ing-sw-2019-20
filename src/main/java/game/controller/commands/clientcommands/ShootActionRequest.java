package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardPower;
import game.model.CardWeapon;
import game.model.effects.FullEffect;

import java.util.List;

/**
 * Request of shoot action, the server will responde with the possible weapon to use
 */
public class ShootActionRequest implements ClientGameMessage {

    /**
     * Handle the message
     * @param handler who handle the message
     */
     @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the message
     * @return the message from the handler
     */
    @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }
}
