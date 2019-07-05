package game.model.effects;

import game.controller.EffectHandler;
import game.controller.commands.ServerGameMessage;
import game.model.Game;
import game.model.Player;
import game.model.Target;

import java.io.Serializable;
import java.util.List;

/**
 * abstract class that describe the common behavior of a single step effect that composes a weapon full effect
 */
public abstract class SimpleEffect implements Serializable {
    /** minimum number of enemy to select */
    protected int minEnemy;
    /** maximum number of enemy to select */
    protected int maxEnemy;
    /** minimum distance from the shooter to select the enemy */
    protected int minDist;
    /** maximum distance from the shooter to select the enemy */
    protected int maxDist;
    /** attribute that indicate where to research in the map the enemy to select */
    protected TargetVisibility visibility;

    /**
     * construct a complete simple effect object with correct parameters
     * @param minEnemy
     * @param maxEnemy
     * @param minDist
     * @param maxDist
     * @param visibility
     */
    public SimpleEffect(int minEnemy, int maxEnemy, int minDist, int maxDist, TargetVisibility visibility) {
        this.minEnemy = minEnemy;
        this.maxEnemy = maxEnemy;
        this.minDist = minDist;
        this.maxDist = maxDist;
        this.visibility = visibility;

    }

    /**
     * construct an empty class object
     */
    public SimpleEffect() {
        
    }

    /**
     * return minEnemy attribute
     * @return minEnemy
     */
    public int getMinEnemy() {
        return minEnemy;
    }

    /**
     * set minEnemy minEnemy attribute
     * @param minEnemy
     */
    public void setMinEnemy(int minEnemy) {
        this.minEnemy = minEnemy;
    }

    /**
     * return maxEnemy attribute
     * @return maxEnemy
     */
    public int getMaxEnemy() {
        return maxEnemy;
    }

    /**
     * set maxEnemy attribute
     * @param maxEnemy
     */
    public void setMaxEnemy(int maxEnemy) {
        this.maxEnemy = maxEnemy;
    }

    /**
     * return minDist attribute
     * @return minDist
     */
    public int getMinDist() {
        return minDist;
    }

    /**
     * set minDist attribute
     * @param minDist
     */
    public void setMinDist(int minDist) {
        this.minDist = minDist;
    }

    /**
     * return maxDist attribute
     * @return maxDist
     */
    public int getMaxDist() {
        return maxDist;
    }

    /**
     * set maxDist attribute
     * @param maxDist
     */
    public void setMaxDist(int maxDist) {
        this.maxDist = maxDist;
    }

    /**
     * return visibility attribute
     * @return visibility
     */
    public TargetVisibility getVisibility() {
        return visibility;
    }

    /**
     * set visibility attribute
     * @param visibility
     */
    public void setVisibility(TargetVisibility visibility) {
        this.visibility = visibility;
    }

    /**
     * research possible target in the map
     * @param shooter
     * @return a list of possible targets
     */
    public abstract List<Target> searchTarget(Player shooter);

    /**
     * apply the effect to the list of targets selected
     * @param shooter
     * @param targets
     */
    public abstract void applyEffect(Player shooter, List<Target> targets);

    /**
     *
     * @param effect
     * @param p
     * @return
     */
    public abstract boolean checkEffect(MovementEffect effect, Player p );

    /**
     *
     * @param effect
     * @param p
     * @return
     */
    public abstract boolean checkEffect(SimpleEffect effect, Player p);

    /**
     *
     * @param h
     * @return
     */
    public abstract ServerGameMessage handle(EffectHandler h);

    /**
     *
     * @param h
     * @param targetList
     * @param model
     * @return
     */
    public abstract ServerGameMessage handleTargetSelection(EffectHandler h, List<Target> targetList, Game model);
}