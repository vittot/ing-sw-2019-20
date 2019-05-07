package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;

public class NotifyPowerUpUsageResponse implements ServerMessage {
    public int id;
    public CardPower cp;

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}