package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;

public class RespawnRequest implements ServerMessage {
    private CardPower cPU;

    public RespawnRequest(CardPower cPU) {
        this.cPU = cPU;
    }

    public CardPower getcPU() {
        return cPU;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
