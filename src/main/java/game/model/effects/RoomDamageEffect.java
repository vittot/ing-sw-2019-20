package game.model.effects;

import game.controller.EffectHandler;
import game.controller.commands.ServerGameMessage;
import game.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * class that represents a simple effect that plain damage to all the players that are positioned in a specific room of the map
 */
public class RoomDamageEffect extends SimpleEffect {
    private int damage; /** attribute representing the damages to apply */
    private int marks; /** attribute representing the marks to apply */

    /**
     * construct a room damage effect object with correct parameters
     * @param minEnemy
     * @param maxEnemy
     * @param minDist
     * @param maxDist
     * @param visibility
     * @param damage
     * @param marks
     */
    public RoomDamageEffect(int minEnemy, int maxEnemy, int minDist, int maxDist, TargetVisibility visibility, int damage, int marks) {
        super(minEnemy, maxEnemy, minDist, maxDist, visibility);
        this.damage = damage;
        this.marks = marks;
    }

    /**
     * return damage attribute value
     * @return damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * set damage attribute value
     * @param damage
     */
    public void setDamage(int damage) {
        this.damage = damage;
    }

    /**
     * return marks attribute value
     * @return marks
     */
    public int getMarks() {
        return marks;
    }

    /**
     * set marks value
     * @param marks
     */
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

    /**
     * answer that this isn't a movement effect object
     * @param effect
     * @param p
     * @return false
     */
    @Override
    public boolean checkEffect(MovementEffect effect, Player p) {
        return false;
    }

    /**
     * control if the effect can be applied by the shooter
     * @param effect
     * @param p
     * @return true/false
     */
    @Override
    public boolean checkEffect(SimpleEffect effect, Player p) {
        if(searchTarget(p) == null)
            return false;
        return true;
    }

    /**
     * handle the room damage effect in collaboration with the server controller
     * @param h
     * @return
     */
    @Override
    public ServerGameMessage handle(EffectHandler h) {
        return h.handle(this);
    }

    /**
     * handle the room damage effect after the target selection in collaboration with the server controller
     * @param h
     * @param targetList
     * @param model
     * @return
     */
    @Override
    public ServerGameMessage handleTargetSelection(EffectHandler h, List<Target> targetList, Game model) {
        List<Target> toApplyEffect = new ArrayList<>();
        for (Target t : model.getMap().getAllRooms()) {
            if (targetList.contains(t))
                toApplyEffect.add(t);
        }
        return h.handleTarget(this, toApplyEffect);
    }

    /**
     * verify if two effects are equals
     * @param o
     * @return true/false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoomDamageEffect that = (RoomDamageEffect) o;
        return damage == that.damage &&
                marks == that.marks;
    }

    /**
     * used by the equals method
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(damage, marks);
    }
}
