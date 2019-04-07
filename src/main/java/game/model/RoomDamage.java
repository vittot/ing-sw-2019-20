package game.model;

import java.util.List;

public class RoomDamage extends Effect{
    private int damage;
    private int marks;

    public RoomDamage(int minEnemy, int maxEnemy, int minDist, int maxDist, int visibility, int damage, int marks) {
        super(minEnemy, maxEnemy, minDist, maxDist, visibility);
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

    public List<List<Target>> searchTarget(Player shooter){}

    public void applyEffect(Player shooter, List<Target>){}
}
