package game.model.effects;

import game.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoomDamageEffect extends Effect{
    private int damage;
    private int marks;

    public RoomDamageEffect(int minEnemy, int maxEnemy, int minDist, int maxDist, TargetVisibility visibility, int damage, int marks) {
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

    /**
     * Return the possible targets. It will have to be verified that they respect the minEnemy - maxEnemy constraint
     * @param shooter
     * @return The List is a singleton for this type of Effect
     */
    public List<Target> searchTarget(Player shooter){
        List<Room> targets;
        Square shooterPos = shooter.getPosition();

        if(visibility == TargetVisibility.VISIBLE)
            targets = shooterPos.getVisibileRooms();
        else
            targets = shooterPos.getMap().getAllRooms();

        List<Target> retList = new ArrayList<>();
        for(Room r: targets)
            retList.add(r);
        return retList;
    }

    /**
     * Apply damage and marks to the targets
     * @param shooter
     * @param targets choosen targets
     */
    public void applyEffect(Player shooter, List<Target> targets){
        for(Target t : targets)
        {
            t.addDamage(shooter,damage);
            t.addThisTurnMarks(shooter,marks);
        }
    }
}
