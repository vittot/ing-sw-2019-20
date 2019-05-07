package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;

public class RespawnRequest implements ServerMessage {
    public CardPower cPU;
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
