package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

public class NotifyTurnChanged implements ServerGameMessage {

    private int currPlayerId;

    public NotifyTurnChanged(int currPlayerId) {
        this.currPlayerId = currPlayerId;
    }

    public int getCurrPlayerId() {
        return currPlayerId;
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
