package game.model.effects;

import game.model.Player;
import game.model.Target;

import java.util.List;

public class SquareDamageEffect extends Effect{
    private int damage;
    private int marks;
    private boolean lastTargetSquare; //if true indicate to search target from the last target position
    private boolean sameDirection;

    public SquareDamageEffect(int minEnemy, int maxEnemy, int minDist, int maxDist, TargetVisibility visibility, int damage, int marks, boolean lastTargetSquare, boolean sameDirection) {
        super(minEnemy, maxEnemy, minDist, maxDist, visibility);
        this.damage = damage;
        this.marks = marks;
        this.lastTargetSquare = lastTargetSquare;
        this.sameDirection = sameDirection;
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

    public boolean isLastTargetSquare() {
        return lastTargetSquare;
    }

    public void setLastTargetSquare(boolean lastTargetSquare) {
        this.lastTargetSquare = lastTargetSquare;
    }

    public boolean isSameDirection() {
        return sameDirection;
    }

    public void setSameDirection(boolean sameDirection) {
        this.sameDirection = sameDirection;
    }

    public List<List<Target>> searchTarget(Player shooter){
        return null;
    }

    public void applyEffect(Player shooter, List<Target> targets){
        //TODO
    }
}
