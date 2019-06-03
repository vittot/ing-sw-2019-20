package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Player;

import java.util.SortedMap;

public class NotifyEndGame implements ServerMessage {
    private SortedMap<Player,Integer> ranking;

    public NotifyEndGame(SortedMap<Player,Integer> gameRanking) {
        this.ranking = gameRanking;
    }


    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

    public SortedMap<Player,Integer> getRanking() {
        return ranking;
    }
}
