package game.model;

public class AoEDamage extends Effect{
    private int damage;
    private int marks;

    public AoEDamage(int minEnemy, int maxEnemy, int visibility, int visibilityAfter, int minDist, int maxDist, int damage, int marks) {
        super(minEnemy, maxEnemy, visibility, visibilityAfter, minDist, maxDist);
        this.damage = damage;
        this.marks = marks;
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
}
