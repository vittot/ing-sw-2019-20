package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.GameMap;
import game.model.Player;

import java.util.ArrayList;
import java.util.List;

public class RejoinGameConfirm implements ServerGameMessage {

    private GameMap map;
    private List<Player> players;
    private int pId;

    public RejoinGameConfirm(GameMap map, List<Player> players, int pId) {
        this.map = map.copy();
        this.players = new ArrayList<>();
        for(Player p : players)
        {
            this.players.add(new Player(p));
        }
        this.pId = pId;
        for(Player p : this.players)
            if(p.getId() != pId)
                p.setCardPower(null);
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
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
