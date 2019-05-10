package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Kill;

public class NotifyDeathResponse implements ServerMessage {
    private Kill kill;

    public NotifyDeathResponse(Kill kill) {
        this.kill = kill;
    }

    public Kill getKill() {
        return kill;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}

