package game.model.effects;

import com.sun.org.apache.bcel.internal.generic.TABLESWITCH;
import game.model.Player;
import game.model.Room;
import game.model.Target;

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
     * Search available room from the shooter
     * @param shooter
     * @return
     */
    public List<List<Target>> searchTarget(Player shooter){
        List<Room> rooms = shooter.getPosition().getVisibleRooms();
        if(minDist == 0){
            rooms.add(new Room(shooter.getPosition().getColor(),shooter.getPosition().getMap()));
        }
        List<Target> retList = new ArrayList<>();
        for(Room r: rooms)
            retList.add(r);
        return Collections.singletonList(retList);
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
