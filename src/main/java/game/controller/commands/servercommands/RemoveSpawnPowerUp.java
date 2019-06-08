package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;
import game.model.exceptions.MapOutOfLimitException;

public class RemoveSpawnPowerUp implements ServerMessage {

    private CardPower powerup;

    public RemoveSpawnPowerUp(CardPower powerUp) {
        this.powerup = powerUp;
    }

    public CardPower getPowerup() {
        return powerup;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
