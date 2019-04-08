package game.model.effects;

import game.model.Player;
import game.model.Target;

import java.util.List;

public class AreaDamageEffect extends Effect{
    private int damage;
    private int marks;
    private int maxEnemyPerSquare;

    public AreaDamageEffect(int minEnemy, int maxEnemy, int minDist, int maxDist, TargetVisibility visibility, int damage, int marks, int maxEnemyPerSquare) {
        super(minEnemy, maxEnemy, minDist, maxDist, visibility);
        this.damage = damage;
        this.marks = marks;
        this.maxEnemyPerSquare = maxEnemyPerSquare;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getMarks() {
        return marks;
    }

    public void setMarks(int marks) {
        this.marks = marks;
    }

    public List<List<Target>> searchTarget(Player shooter){
        return null;
    }

    public void applyEffect(Player shooter, List<Target> targets){//TODO
    }
}