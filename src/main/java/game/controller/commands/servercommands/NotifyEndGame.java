package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.Player;
import game.model.Ranking;

import java.util.SortedMap;

/**
 * notify the end of the game
 */
public class NotifyEndGame implements ServerGameMessage {
    /**
     * final game ranking
     */
    private Ranking ranking;

    /**
     * construct correct message
     * @param gameRanking ranking
     */
    public NotifyEndGame(Ranking gameRanking) {
        this.ranking = gameRanking;
    }

    /**
     * Handle the message
     * @param handler handler
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler handler
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * return final game ranking
     * @return ranking
     */
    public Ranking getRanking() {
        return ranking;
    }
}
