package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.CardPower;

public class RespawnResponse implements ClientMessage {
    private CardPower powerUp;

    public RespawnResponse(CardPower powerUp) {
        this.powerUp = powerUp;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) {
        return handler.handle(this);
    }

    public CardPower getPowerUp() {
        return powerUp;
    }
}
