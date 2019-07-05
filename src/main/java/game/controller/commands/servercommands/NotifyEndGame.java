package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.Player;

import java.util.SortedMap;

/**
 * notify the end of the game
 */
public class NotifyEndGame implements ServerGameMessage {
    /**
     * sorted map containing the players in order of occurred points
     */
    private SortedMap<Player,Integer> ranking;

    /**
     * construct correct message
     * @param gameRanking
     */
    public NotifyEndGame(SortedMap<Player,Integer> gameRanking) {
        this.ranking = gameRanking;
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

    /**
     * return final game ranking
     * @return ranking
     */
    public SortedMap<Player,Integer> getRanking() {
        return ranking;
    }
}
