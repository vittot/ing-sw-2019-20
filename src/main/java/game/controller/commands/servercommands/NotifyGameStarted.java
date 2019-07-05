package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.*;

import java.util.ArrayList;
import java.util.List;

/**
 * notify the game has started
 */
public class NotifyGameStarted implements ServerGameMessage {
    /**
     * map of the game
     */
    private GameMap map;
    /**
     * players that joined the waiting room
     */
    private List<Player> players;
    /**
     * attribute that specifies the game id
     */
    private int id = 0;
    /**
     * killBoard that will be updated whenever a player will die
     */
    private List<Kill> killBoard;

    /**
     * construct correct message
     * @param map
     * @param players
     * @param id
     * @param killBoard
     */
    public NotifyGameStarted(GameMap map, List<Player> players, int id, List<Kill> killBoard) {
        this.map = map.copy();
        this.players = new ArrayList<>();
        this.killBoard = killBoard;
        for(Player p : players)
        {
            this.players.add(new Player(p));
        }
        this.id = id;
        for(Player p : this.players)
            if(p.getId() != id)
                p.setCardPower(null);
    }

    /**
     * return the game id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * return the list of players
     * @return players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * return the map
     * @return map
     */
    public GameMap getMap() {
        return map;
    }

    /**
     * return the killBoard
     * @return killBoard
     */
    public List<Kill> getKillBoard() {
        return killBoard;
    }

    /**
     * Handle the message
     * @param handler who handle the messag
     * @param handler
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the messag
     * @param handler
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
