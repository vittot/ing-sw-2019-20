package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;

public class NotifyTurnChanged implements ServerMessage {

    private int currPlayerId;

    public NotifyTurnChanged(int currPlayerId) {
        this.currPlayerId = currPlayerId;
    }

    public int getCurrPlayerId() {
        return currPlayerId;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
