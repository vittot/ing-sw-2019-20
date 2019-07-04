package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.GameMap;
import game.model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Send the confirm rejoin connection
 */
public class RejoinGameConfirm implements ServerGameMessage {
    /**
     * Game map
     */
    private GameMap map;
    /**
     * List of player in the rejoined game
     */
    private List<Player> players;
    /**
     * Player reconnected id
     */
    private int pId;

    /**
     * Constructor
     * @param map map
     * @param players player
     * @param pId player id
     */
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

    /**
     *
     * @return the id
     */
    public int getId() {
        return pId;
    }

    /**
     *
     * @return the game GameMap
     */
    public GameMap getMap() {
        return map;
    }

    /**
     *
     * @return the player i the game
     */
    public List<Player> getPlayers() {
        return players;
    }
    /**
     * Handle the message
     * @param handler who handle the message
     */

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }
    /**
     * Handle the message
     * @param handler who handle the message
     */

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
