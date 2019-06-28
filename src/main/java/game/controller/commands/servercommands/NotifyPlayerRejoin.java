package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

public class NotifyPlayerRejoin implements ServerGameMessage {

    private int playerId;

    public NotifyPlayerRejoin(int id) {
        this.playerId = id;
    }

    public int getPlayerId() {
        return playerId;
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
