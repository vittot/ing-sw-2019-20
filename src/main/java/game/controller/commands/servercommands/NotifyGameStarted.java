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

    private GameMap map; /** map of the game */
    private List<Player> players; /** players that joined the waiting room */
    private int id = 0; /** attribute that specifies the game id */
    private List<Kill> killBoard; /** killBoard that will be updated whenever a player will die */

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
