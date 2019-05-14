package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Game;
import game.model.GameMap;
import game.model.Kill;

import java.util.List;

public class NotifyGameStarted implements ServerMessage {

    private GameMap map;

    public NotifyGameStarted(GameMap map) {
        this.map = map;
    }

    public GameMap getMap() {
        return map;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
