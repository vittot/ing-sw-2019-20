package game.model;

import java.util.List;
import java.util.Map;

public interface GameListener {
    public void onChangeTurn(Player p);
    public void onGameEnd(Map<Player,Integer> gameRanking);

    public void onDamage(Player damaged, Player attacker, int damage);
    public void onMarks(Player marked, Player marker, int marks);
    public void onDeath(Player dead);
    void onDeath(Kill kill);
    public void onGrabWeapon(Player p, CardWeapon cw);
    public void onGrabCardAmmo(Player p, List<Color> ammo, List<CardPower> powerups);
    public void onMove(Player p);
    public void onRespawn(Player p);
    public void onPowerUpUse(Player p, CardPower c);
}
