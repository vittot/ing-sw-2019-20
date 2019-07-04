package game.model;

import java.util.List;
import java.util.SortedMap;

/**
 * interface that describe the behavior of client handler object to allow the notification of various game events
 */
public interface GameListener {

    /**
     * notify the turn is changed and now it's player p time
     * @param p
     */
    void onChangeTurn(Player p);

    /**
     * notify game has ended
     * @param gameRanking
     */
    void onGameEnd(SortedMap<Player,Integer> gameRanking);

    /**
     * notify damages that a player dealt to another one
     * @param damaged
     * @param attacker
     * @param damage
     * @param marksToRemove
     */
    void onDamage(Player damaged, Player attacker, int damage, int marksToRemove);

    /**
     * notify that a player give marks to another one
     * @param marked
     * @param marker
     * @param marks
     */
    void onMarks(Player marked, Player marker, int marks);

    /**
     * notify a player death
     * @param kill
     */
    void onDeath(Kill kill);

    /**
     * notify a weapon grab by a player
     * @param p
     * @param cw
     * @param cww
     */
    void onGrabWeapon(Player p, CardWeapon cw, CardWeapon cww);

    /**
     * notify that a player grab a card ammo from the field
     * @param p
     * @param ammo
     */
    void onGrabCardAmmo(Player p, List<Color> ammo);

    /**
     * notify a player movement
     * @param p
     */
    void onMove(Player p);

    /**
     * notify a player respawn
     * @param p
     */
    void onRespawn(Player p);

    /**
     * notify the utilization of a power-up effect
     * @param p
     * @param c
     */
    void onPowerUpUse(Player p, CardPower c);

    /**
     * notify that a player has been suspended from the game
     * @param p
     * @param timeOut
     */
    void onPlayerSuspend(Player p, boolean timeOut);

    /**
     * notify that a player rejoined the game
     * @param player
     */
    void onPlayerRejoined(Player player);

    /**
     * notify the replacement of a weapon on a square respawn point
     * @param cw
     * @param s
     */
    void onReplaceWeapon(CardWeapon cw, Square s);

    /**
     * the replacement of a card ammo on a square
     * @param ca
     * @param s
     */
    void onReplaceAmmo(CardAmmo ca, Square s);

    /**
     * notify the update of player marks adding the marks accumulated during the last turn action
     * @param player
     */
    void onPlayerUpdateMarks(Player player);

    /**
     * notify that a player was raged after he was already killed
     * @param lastKill
     */
    void onPlayerRaged(Kill lastKill);

    /**
     * Return the username
     * @return username
     */
    String getUsername();

    /**
     * notify the starting of the final frenzy phase
     */
    void onFinalFrenzy();
}
