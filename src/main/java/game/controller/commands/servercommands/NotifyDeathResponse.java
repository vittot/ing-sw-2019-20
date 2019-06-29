package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.Kill;

public class NotifyDeathResponse implements ServerGameMessage {
    private Kill kill;

    public NotifyDeathResponse(Kill kill) {
        this.kill = kill;
    }

    public Kill getKill() {
        return kill;
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

