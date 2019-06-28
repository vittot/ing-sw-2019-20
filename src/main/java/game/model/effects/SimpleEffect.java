package game.model.effects;

import game.controller.EffectHandler;
import game.controller.commands.ServerGameMessage;
import game.model.Game;
import game.model.Player;
import game.model.Target;

import java.io.Serializable;
import java.util.List;

public abstract class SimpleEffect implements Serializable {
    protected int minEnemy;
    protected int maxEnemy;
    protected int minDist;
    protected int maxDist;
    protected TargetVisibility visibility;



    public SimpleEffect(int minEnemy, int maxEnemy, int minDist, int maxDist, TargetVisibility visibility) {
        this.minEnemy = minEnemy;
        this.maxEnemy = maxEnemy;
        this.minDist = minDist;
        this.maxDist = maxDist;
        this.visibility = visibility;

    }

    public SimpleEffect() {
        
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

    public abstract List<Target> searchTarget(Player shooter);

    public abstract void applyEffect(Player shooter, List<Target> targets);

    public abstract boolean checkEffect(MovementEffect effect, Player p );

    public abstract boolean checkEffect(SimpleEffect effect, Player p);

    public abstract ServerGameMessage handle(EffectHandler h);

    public abstract ServerGameMessage handleTargetSelection(EffectHandler h, List<Target> targetList, Game model);
}