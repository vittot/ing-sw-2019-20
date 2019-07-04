package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;

/**
 * Remove the power up used for respawn
 */
public class RemoveSpawnPowerUp implements ServerGameMessage {
    /**
     * Power up used for spawn
     */
    private CardPower powerup;

    /**
     * Constructor
     * @param powerUp power card
     */
    public RemoveSpawnPowerUp(CardPower powerUp) {
        this.powerup = powerUp;
    }

    /**
     *
     * @return the card power
     */
    public CardPower getPowerup() {
        return powerup;
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
