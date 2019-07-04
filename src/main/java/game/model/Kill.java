package game.model;

import java.io.Serializable;

/**
 * class representing every player death, indicate the killer and if the victim was raged too
 */
public class Kill implements Serializable {
    private Player killer; /** reference to the killer player */
    private Player victim; /** reference to the victim (player dead) */
    private boolean isRage; /** boolean that specifies if the victim was raged too */

    /**
     * construct a Kill object
     * @param killer
     * @param victim
     * @param isRage
     */
    public Kill(Player killer, Player victim, boolean isRage) {
        this.killer = killer;
        this.victim = victim;
        this.isRage = isRage;
    }

    /**
     * return killer reference
     * @return killer
     */
    public Player getKiller() {
        return killer;
    }

    /**
     * set killer reference
     * @param killer
     */
    public void setKiller(Player killer) {
        this.killer = killer;
    }

    /**
     * return victim reference
     * @return victim
     */
    public Player getVictim() {
        return victim;
    }

    /**
     * set victim reference
     * @param victim
     */
    public void setVictim(Player victim) {
        this.victim = victim;
    }

    /**
     * return if the victim was raged
     * @return isRage
     */
    public boolean isRage() {
        return isRage;
    }

    /**
     * set isRage attribute
     * @param rage
     */
    public void setRage(boolean rage) {
        isRage = rage;
    }
}
