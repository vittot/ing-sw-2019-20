package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

public class NotifyPlayerSuspend implements ServerGameMessage {

    private int pId;

    public NotifyPlayerSuspend(int id) {
        this.pId = id;
    }

    public int getpId() {
        return pId;
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
