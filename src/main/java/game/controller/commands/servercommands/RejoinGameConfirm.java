package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.GameMap;
import game.model.Player;
import game.model.exceptions.MapOutOfLimitException;

import java.util.List;

public class RejoinGameConfirm implements ServerMessage {

    private GameMap map;
    private List<Player> players;
    private int pId;

    public RejoinGameConfirm(GameMap map, List<Player> players, int pId) {
        this.map = map;
        this.players = players;
        this.pId = pId;
    }

    public int getId() {
        return pId;
    }

    public GameMap getMap() {
        return map;
    }

    public List<Player> getPlayers() {
        return players;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
