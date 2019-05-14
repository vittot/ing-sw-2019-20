package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Player;
import java.util.Map;

public class NotifyEndGameResponse implements ServerMessage {
    private Map<Player,Integer> ranking;

    public NotifyEndGameResponse(Map<Player, Integer> gameRanking) {
        this.ranking = gameRanking;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

    public Map<Player, Integer> getRanking() {
        return ranking;
    }
}
