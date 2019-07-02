package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;

public class RespawnRequest implements ServerGameMessage {
    private CardPower cPU;

    public RespawnRequest(CardPower cPU) {
        this.cPU = cPU;
    }

    public RespawnRequest()
    {
        this.cPU = null;
    }

    public CardPower getcPU() {
        return cPU;
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
