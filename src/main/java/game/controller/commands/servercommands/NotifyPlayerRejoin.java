package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.exceptions.MapOutOfLimitException;

public class NotifyPlayerRejoin implements ServerMessage {

    private int playerId;

    public NotifyPlayerRejoin(int id) {
        this.playerId = id;
    }

    public int getPlayerId() {
        return playerId;
    }

    @Override
    public void handle(ServerMessageHandler handler) throws MapOutOfLimitException {
        handler.handle(this);
    }
}
