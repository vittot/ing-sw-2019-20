package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.model.CardPower;

public class RespawnResponse implements ClientMessage {
    public CardPower powerUp;
    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
