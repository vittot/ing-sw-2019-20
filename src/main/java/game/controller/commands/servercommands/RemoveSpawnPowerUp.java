package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;

public class RemoveSpawnPowerUp implements ServerGameMessage {

    private CardPower powerup;

    public RemoveSpawnPowerUp(CardPower powerUp) {
        this.powerup = powerUp;
    }

    public CardPower getPowerup() {
        return powerup;
    }

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
