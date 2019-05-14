package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.CardPower;

public class ChoosePowerUpResponse implements ClientMessage {
    private CardPower cardPower;

    public ChoosePowerUpResponse(CardPower cardPower) {
        this.cardPower = cardPower;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) {
        return handler.handle(this);
    }
}
