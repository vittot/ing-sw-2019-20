package game.model;

import java.util.Map;

public interface GameListener {
    public void onChangeTurn(Player p);
    public void onGameEnd(Map<Player,Integer> gameRanking);
}
