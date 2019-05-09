package game.model;

public interface GameListener {
    public void onChangeTurn(Player p);
    public void onGameEnd(Player p);
}
