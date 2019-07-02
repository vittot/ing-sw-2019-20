package game.model;

import java.util.List;
import java.util.SortedMap;

public interface GameListener {

    void onChangeTurn(Player p);
    void onGameEnd(SortedMap<Player,Integer> gameRanking);
    void onDamage(Player damaged, Player attacker, int damage, int marksToRemove);
    void onMarks(Player marked, Player marker, int marks);
    void onDeath(Kill kill);
    void onGrabWeapon(Player p, CardWeapon cw, CardWeapon cww);
    void onGrabCardAmmo(Player p, List<Color> ammo);
    void onMove(Player p);
    void onRespawn(Player p);
    void onPowerUpUse(Player p, CardPower c);
    void onPlayerSuspend(Player p, boolean timeOut);
    void onPlayerRejoined(Player player);
    void onReplaceWeapon(CardWeapon cw, Square s);
    void onReplaceAmmo(CardAmmo ca, Square s);
    void onPlayerUpdateMarks(Player player);
    void onPlayerRaged(Kill lastKill);
    String getUsername();
}
