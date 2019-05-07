package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Player;

import java.util.List;

public class NotifyEndGameResponse implements ServerMessage {
    public List<Player> ranking;

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
