package game.model;

public class SquareDamage extends Effect{
    private int damage;
    private int marks;
    private boolean lastTargetSquare;
    private boolean sameDirection;

    public SquareDamage(int minEnemy, int maxEnemy, int minDist, int maxDist, int visibility, int damage, int marks, boolean lastTargetSquare, boolean sameDirection) {
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
}
