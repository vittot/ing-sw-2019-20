package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.*;

import java.util.ArrayList;
import java.util.List;

public class NotifyGameStarted implements ServerGameMessage {

    private GameMap map;
    private List<Player> players;
    private int id = 0;

    public NotifyGameStarted(GameMap map, List<Player> players, int id) {
        this.map = map.copy();
        this.players = new ArrayList<>();
        for(Player p : players)
        {
            this.players.add(new Player(p));
        }
        this.id = id;
        for(Player p : this.players)
            if(p.getId() != id)
                p.setCardPower(null);
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
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
