package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardPower;

/**
 * Request of respawn in the selected color
 */
public class RespawnResponse implements ClientGameMessage {
    /**
     * Power up wasted to respawn
     */
    private CardPower powerUp;

    /**
     * Constructor
     * @param powerUp used for color selection
     */
    public RespawnResponse(CardPower powerUp) {
        this.powerUp = powerUp;
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */
     @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }

    /**
     *
     * @return the power up selected
     */
    public CardPower getPowerUp() {
        return powerUp;
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
