package game.model.effects;

import game.controller.EffectHandler;
import game.controller.commands.ServerMessage;
import game.model.Player;
import game.model.Room;
import game.model.Square;
import game.model.Target;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RoomDamageEffect extends SimpleEffect {
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
     * @return The List is a singleton for this type of SimpleEffect
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

    @Override
    public boolean checkEffect(MovementEffect effect, Player p) {
        return false;
    }


    @Override
    public boolean checkEffect(SimpleEffect effect, Player p) {
        if(searchTarget(p) == null)
            return false;
        return true;
    }

    @Override
    public ServerMessage handle(EffectHandler h) {
        return h.handle(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomDamageEffect that = (RoomDamageEffect) o;
        return damage == that.damage &&
                marks == that.marks;
    }

    @Override
    public int hashCode() {
        return Objects.hash(damage, marks);
    }
}
