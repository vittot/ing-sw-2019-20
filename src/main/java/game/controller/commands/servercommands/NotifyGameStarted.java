package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Game;

public class NotifyGameStarted implements ServerMessage {

    private Game game;

    public NotifyGameStarted(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
