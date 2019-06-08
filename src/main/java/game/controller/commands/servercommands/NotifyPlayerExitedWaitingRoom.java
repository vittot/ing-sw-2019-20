package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.exceptions.MapOutOfLimitException;

public class NotifyPlayerExitedWaitingRoom implements ServerMessage {

    private int playerId;

    public NotifyPlayerExitedWaitingRoom(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
