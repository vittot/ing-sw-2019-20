package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Kill;

public class NotifyDeathResponse implements ServerMessage {
    public Kill kill;
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}

