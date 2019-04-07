package game.model;

import java.util.List;

public class PlainDamageEffect extends Effect{
    private int damage;
    private int marks;
    private boolean lastTarget; //if the target has to be the last hit by the weapon
    private DifferentTarget differentTarget; //0=target can be one of the lasts, 1=target cannot be the last one, 2=target cannot be one of the previews shoot
    private boolean chainTarget; //if the target should be found from the target of the last effect (eg: T.H.O.R.)
    private boolean sameDirection; //if the next target has to be on the same last direction

    public PlainDamageEffect(int minEnemy, int maxEnemy, int minDist, int maxDist, TargetVisibility visibility, int damage, int marks, boolean lastTarget, DifferentTarget differentTarget, boolean chainTarget, boolean sameDirection) {
        super(minEnemy, maxEnemy, minDist, maxDist, visibility);
        this.damage = damage;
        this.marks = marks;
        this.lastTarget = lastTarget;
        this.differentTarget = differentTarget;
        this.chainTarget = chainTarget;
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

    public boolean isLastTarget() {
        return lastTarget;
    }

    public void setLastTarget(boolean lastTarget) {
        this.lastTarget = lastTarget;
    }

    public DifferentTarget getDifferentTarget() {
        return differentTarget;
    }

    public void setDifferentTarget(DifferentTarget differentTarget) {
        this.differentTarget = differentTarget;
    }

    public boolean isChainTarget() {
        return chainTarget;
    }

    public void setChainTarget(boolean chainTarget) {
        this.chainTarget = chainTarget;
    }

    public boolean isSameDirection() {
        return sameDirection;
    }

    public void setSameDirection(boolean sameDirection) {
        this.sameDirection = sameDirection;
    }

    public List<List<Target>> searchTarget(Player shooter){}

    public void applyEffect(Player shooter, List<Target>){}
}
