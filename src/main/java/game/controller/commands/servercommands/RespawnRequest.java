package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;

/**
 * Request of respawn, and give the new power up to the client
 */
public class RespawnRequest implements ServerGameMessage {
    /**
     * Card power given to the player
     */
    private CardPower cPU;

    /**
     * Constructor
     * @param cPU card power
     */
    public RespawnRequest(CardPower cPU) {
        this.cPU = cPU;
    }

    /**
     * Constructor
     */
    public RespawnRequest()
    {
        this.cPU = null;
    }

    /**
     *
     * @return the card power
     */
    public CardPower getcPU() {
        return cPU;
    }
    /**
     * Handle the message
     * @param handler who handle the message
     */

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }
    /**
     * Handle the message
     * @param handler who handle the message
     */

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
