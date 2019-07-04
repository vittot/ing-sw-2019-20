package game.model.effects;

import game.controller.EffectHandler;
import game.controller.commands.ServerGameMessage;
import game.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * class that represents a simple effect that plain damage to an established number of targets
 */
public class PlainDamageEffect extends SimpleEffect {
    private int damage; /** attribute that contains the damages to apply to the targets */
    private int marks; /** attribute that contains the marks to apply to the targets */
    private boolean lastTarget; /** if the target has to be among the previous targets hit by the weapon */
    private DifferentTarget differentTarget; /** the target could be different from the last one or from all the previous */
    private boolean chainTarget; /** if the target should be found from the target of the last effects (eg: T.H.O.R.) */
    private boolean sameDirection; /** if the target has to be on the same last direction of the last one */

    /**
     * construct a complete plain damage effect object with the correct parameters
     * @param minEnemy
     * @param maxEnemy
     * @param minDist
     * @param maxDist
     * @param visibility
     * @param damage
     * @param marks
     * @param lastTarget
     * @param differentTarget
     * @param chainTarget
     * @param sameDirection
     */
    public PlainDamageEffect(int minEnemy, int maxEnemy, int minDist, int maxDist, TargetVisibility visibility, int damage, int marks, boolean lastTarget, DifferentTarget differentTarget, boolean chainTarget, boolean sameDirection) {
        super(minEnemy, maxEnemy, minDist, maxDist, visibility);
        this.damage = damage;
        this.marks = marks;
        this.lastTarget = lastTarget;
        this.differentTarget = differentTarget;
        this.chainTarget = chainTarget;
        this.sameDirection = sameDirection;
    }

    /**
     * return damage attribute
     * @return damage
     */
    public int getDamage() {return damage; }

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
     * set marks attribute value
     * @param marks
     */
    public void setMarks(int marks) {
        this.marks = marks;
    }

    /**
     * return lastTarget attribute
     * @return lastTarget
     */
    public boolean isLastTarget() {
        return lastTarget;
    }

    /**
     * set lastTarget attribute
     * @param lastTarget
     */
    public void setLastTarget(boolean lastTarget) {
        this.lastTarget = lastTarget;
    }

    /**
     * return differentTarget attribute
     * @return differentTarget
     */
    public DifferentTarget getDifferentTarget() {
        return differentTarget;
    }

    /**
     * set differentTarget attribute
     * @param differentTarget
     */
    public void setDifferentTarget(DifferentTarget differentTarget) {
        this.differentTarget = differentTarget;
    }

    /**
     * return chainTarget attribute
     * @return chainTarget
     */
    public boolean isChainTarget() {
        return chainTarget;
    }

    /**
     * set chainTarget attribute
     * @param chainTarget
     */
    public void setChainTarget(boolean chainTarget) {
        this.chainTarget = chainTarget;
    }

    /**
     * return sameDirection attribute
     * @return sameDirection
     */
    public boolean isSameDirection() {
        return sameDirection;
    }

    /**
     * set sameDirection attribute
     * @param sameDirection
     */
    public void setSameDirection(boolean sameDirection) {
        this.sameDirection = sameDirection;
    }

    /**
     * Return the possible targets. It will have to be verified that they respect the minEnemy - maxEnemy constraint
     * @param shooter
     * @return The List is a singleton for this type of SimpleEffect
     */
    public List<Target> searchTarget(Player shooter){

        List<Player> targets;
        List<Player> prevTargets = null;
        Square shooterPos = chainTarget ? shooter.getActualWeapon().getLastTarget().getPosition() : shooter.getPosition();
        if(shooter.getActualWeapon() != null)
            prevTargets = shooter.getActualWeapon().getPreviousTargets();
        if(lastTarget){
            targets = prevTargets.stream().filter(p -> GameMap.distanceBtwSquares(shooterPos,p.getPosition())<=maxDist && GameMap.distanceBtwSquares(shooterPos,p.getPosition())>=minDist).collect(Collectors.toList());
        }
        else{
            switch(visibility){
                case VISIBLE:
                    targets = shooterPos.getVisiblePlayers(minDist,maxDist);
                    break;
                case INVISIBLE:
                    targets = shooterPos.getInvisiblePlayers(minDist,maxDist);
                    break;
                case DIRECTION:
                    targets = shooterPos.getPlayersInDirections(minDist,maxDist);
                    break;
                default:
                    targets = shooterPos.getMap().getAllPlayers();
            }

        }
        if(differentTarget == DifferentTarget.NOTTHELAST)
                    targets.remove(shooter.getActualWeapon().getLastTarget());

        else if(differentTarget == DifferentTarget.NONEOFTHEPREVIOUS)
                    targets.removeAll(shooter.getActualWeapon().getPreviousTargets());

        if(sameDirection)
        {
            Direction lastDir = shooter.getActualWeapon().getLastDirection();
            Square lastTargPos = shooter.getActualWeapon().getLastTargetSquare();
            switch(lastDir)
            {

                case UP:
                    targets = targets.stream().filter(t -> t.getPosition().getY() <= lastTargPos.getY()).collect(Collectors.toList());
                    break;
                case DOWN:
                    targets = targets.stream().filter(t -> t.getPosition().getY() >= lastTargPos.getY()).collect(Collectors.toList());
                    break;
                case LEFT:
                    targets = targets.stream().filter(t -> t.getPosition().getX() <= lastTargPos.getX()).collect(Collectors.toList());
                    break;
                case RIGHT:
                    targets = targets.stream().filter(t -> t.getPosition().getX() >= lastTargPos.getX()).collect(Collectors.toList());
                    break;
            }
        }

        targets.remove(shooter);

        List<Target> retList = new ArrayList<>();
        for(Player p: targets)
            retList.add(p);

        return retList;

    }

    /**
     * Apply damage and marks to the targets
     * @param shooter
     * @param targets choosen targets
     */
    public void applyEffect(Player shooter, List<Target> targets){
        List<Player> prevTargets = shooter.getActualWeapon().getPreviousTargets();
        List<Player> toShoot = new ArrayList<>();
        for(Target t : targets)
            toShoot.add((Player)t);
        for(Player t : toShoot)
        {
            if(!t.equals(shooter)) {
                t.addDamage(shooter, damage);
                t.addThisTurnMarks(shooter, marks);
                prevTargets.remove(t);
                prevTargets.add(t);
            }
        }
        shooter.getActualWeapon().setLastTargetSquare(prevTargets.get(prevTargets.size()-1).getPosition());
        shooter.getActualWeapon().setLastDirection(GameMap.getDirection(shooter.getPosition(),shooter.getActualWeapon().getLastTarget().getPosition()));
    }

    /**
     * answer that this isn't a movement effect
     * @param effect
     * @param p
     * @return
     */
    @Override
    public boolean checkEffect(MovementEffect effect, Player p) {
        return false;
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
        PlainDamageEffect that = (PlainDamageEffect) o;
        return damage == that.damage &&
                marks == that.marks &&
                lastTarget == that.lastTarget &&
                chainTarget == that.chainTarget &&
                sameDirection == that.sameDirection &&
                differentTarget == that.differentTarget;
    }

    /**
     * used by the equals method
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(damage, marks, lastTarget, differentTarget, chainTarget, sameDirection);
    }

    /**
     * check if the effect can be used
     * @param effect
     * @param p
     * @return
     */
    @Override
    public boolean checkEffect(SimpleEffect effect, Player p) {
        if(searchTarget(p) == null)
            return false;
        return true;
    }

    /**
     * handle plain damange effect
     * @param h
     * @return
     */
    @Override
    public ServerGameMessage handle(EffectHandler h) {
        return h.handle(this);
    }

    /**
     * handle plain damage effect after target selection
     * @param h
     * @param targetList
     * @param model
     * @return
     */
    @Override
    public ServerGameMessage handleTargetSelection(EffectHandler h, List<Target> targetList, Game model) {
        List<Target> toApplyEffect = new ArrayList<>();
        for (Target t : model.getPlayers()) {
            if (targetList.contains(t))
                toApplyEffect.add(t);
        }
        return h.handleTarget(this, toApplyEffect);
    }
}
