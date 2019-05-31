package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.exceptions.MapOutOfLimitException;

public class NotifyPlayerSuspend implements ServerMessage {

    private int pId;

    public NotifyPlayerSuspend(int id) {
        this.pId = id;
    }

    public int getpId() {
        return pId;
    }

    @Override
    public void handle(ServerMessageHandler handler) throws MapOutOfLimitException {
        handler.handle(this);
    }
}
