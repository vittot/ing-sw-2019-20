package game.model;

import java.util.List;
import java.util.Map;

public interface GameListener {

    void onChangeTurn(Player p);
    void onGameEnd(Map<Player,Integer> gameRanking);
    void onDamage(Player damaged, Player attacker, int damage);
    void onMarks(Player marked, Player marker, int marks);
    void onDeath(Player dead);
    void onDeath(Kill kill);
    void onGrabWeapon(Player p, CardWeapon cw);
    void onGrabCardAmmo(Player p, List<Color> ammo);
    void onMove(Player p);
    void onRespawn(Player p);
    void onPowerUpUse(Player p, CardPower c);
    void onPlayerSuspend(Player p);
    void onPlayerRejoined(Player player);
}
