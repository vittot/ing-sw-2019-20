package game.model.effects;

import game.model.Player;
import game.model.Target;

import java.util.List;

public abstract class Effect {
    protected int minEnemy;
    protected int maxEnemy;
    protected int minDist;
    protected int maxDist;
    protected TargetVisibility visibility;



    public Effect(int minEnemy, int maxEnemy, int minDist, int maxDist, TargetVisibility visibility) {
        this.minEnemy = minEnemy;
        this.maxEnemy = maxEnemy;
        this.minDist = minDist;
        this.maxDist = maxDist;
        this.visibility = visibility;

    }


    public int getMinEnemy() {
        return minEnemy;
    }

    public void setMinEnemy(int minEnemy) {
        this.minEnemy = minEnemy;
    }

    public int getMaxEnemy() {
        return maxEnemy;
    }

    public void setMaxEnemy(int maxEnemy) {
        this.maxEnemy = maxEnemy;
    }

    public int getMinDist() {
        return minDist;
    }

    public void setMinDist(int minDist) {
        this.minDist = minDist;
    }

    public int getMaxDist() {
        return maxDist;
    }

    public void setMaxDist(int maxDist) {
        this.maxDist = maxDist;
    }

    public TargetVisibility getVisibility() {
        return visibility;
    }

    public void setVisibility(TargetVisibility visibility) {
        this.visibility = visibility;
    }

    public abstract List<List<Target>> searchTarget(Player shooter);

    public abstract void applyEffect(Player shooter, List<Target> targets);
}