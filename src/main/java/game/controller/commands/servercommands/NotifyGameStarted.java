package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.*;

import java.util.List;

public class NotifyGameStarted implements ServerMessage {

    private GameMap map;
    private List<Player> players;
    private int id = 0;

    public NotifyGameStarted(List<Player> players, GameMap map) {
        this.map = map;
        this.players = players;
    }

    public NotifyGameStarted(GameMap map, List<Player> players, int id) {
        this.map = map;
        this.players = players;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public GameMap getMap() {
        return map;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
