package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Player;

import java.util.List;

public class NotifyMovementResponse implements ServerMessage {
    public int id;
    public int x, y;

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
