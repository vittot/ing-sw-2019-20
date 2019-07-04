package game.model.effects;

import game.controller.EffectHandler;
import game.controller.commands.ServerGameMessage;
import game.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * class that represents a simple effect that plain damage to aall the players in a specific square of a map
 */
public class SquareDamageEffect extends SimpleEffect {
    private int damage; /** attribute that contains the damages to apply to the targets */
    private int marks; /** attribute that contains the marks to apply to the targets */
    private boolean lastTargetSquare; /** if true indicate to search target from the last target position */
    private boolean sameDirection; /** true if the next target has to be select in the same direction of the previous */

    /**
     * construct a complete square damage effect with correct parameters
     * @param minEnemy
     * @param maxEnemy
     * @param minDist
     * @param maxDist
     * @param visibility
     * @param damage
     * @param marks
     * @param lastTargetSquare
     * @param sameDirection
     */
    public SquareDamageEffect(int minEnemy, int maxEnemy, int minDist, int maxDist, TargetVisibility visibility, int damage, int marks, boolean lastTargetSquare, boolean sameDirection) {
        super(minEnemy, maxEnemy, minDist, maxDist, visibility);
        this.damage = damage;
        this.marks = marks;
        this.lastTargetSquare = lastTargetSquare;
        this.sameDirection = sameDirection;
    }

    /**
     * return damage attribute
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
     * return marks attribute
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
     * return lastTargetSquare attribute
     * @return lastTargetSquare
     */
    public boolean isLastTargetSquare() {
        return lastTargetSquare;
    }

    /**
     * set lastTargetSquare attribute
     * @param lastTargetSquare
     */
    public void setLastTargetSquare(boolean lastTargetSquare) {
        this.lastTargetSquare = lastTargetSquare;
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

        List<Square> targets;
        Square shooterPos = lastTargetSquare ? shooter.getActualWeapon().getLastTargetSquare() : shooter.getPosition();
        switch(visibility){
            case VISIBLE:
                targets = shooterPos.getVisibleSquares(minDist,maxDist);
                break;
            case INVISIBLE:
                targets = shooterPos.getInvisibleSquares(minDist,maxDist);
                break;
            case DIRECTION:
                targets = shooterPos.getSquaresInDirections(minDist,maxDist);
                break;
            default:
                targets = shooterPos.getMap().getAllSquares();
        }

        if(sameDirection)
        {
            Direction lastDir = shooter.getActualWeapon().getLastDirection();
            Square lastTargPos = shooter.getActualWeapon().getLastTargetSquare();
            switch(lastDir)
            {

                case UP:
                    targets = targets.stream().filter(t -> t.getY() < lastTargPos.getY()).collect(Collectors.toList());
                    break;
                case DOWN:
                    targets = targets.stream().filter(t -> t.getY() > lastTargPos.getY()).collect(Collectors.toList());
                    break;
                case LEFT:
                    targets = targets.stream().filter(t -> t.getX() < lastTargPos.getX()).collect(Collectors.toList());
                    break;
                case RIGHT:
                    targets = targets.stream().filter(t -> t.getX() > lastTargPos.getX()).collect(Collectors.toList());
                    break;
            }
        }
        List<Target> retList;
        //control that allow to consider also square where there isn't any player
        if(damage == 0 && marks == 0)
            retList = new ArrayList<>(targets);
        else {
            retList = new ArrayList<>();
            for (Square s : targets)
                if (!s.getPlayers().isEmpty())
                    retList.add(s);
        }
        return retList;

    }

    /**
     * Apply damage and marks to the targets
     * @param shooter
     * @param targets choosen targets
     */
    public void applyEffect(Player shooter, List<Target> targets) {
        List<Player> prevTargets = shooter.getActualWeapon().getPreviousTargets();
        Square s;
        for (Target t : targets) {
            s = (Square) t;
            t.addDamage(shooter, damage);
            t.addThisTurnMarks(shooter, marks);
            prevTargets.removeAll(s.getPlayers());
            prevTargets.addAll(s.getPlayers());
        }
        if (prevTargets.size() == 0) {
            shooter.getActualWeapon().setLastTargetSquare((Square) targets.get(0));
            shooter.getActualWeapon().setLastDirection(GameMap.getDirection(shooter.getPosition(), (Square) targets.get(0)));
        } else {
            shooter.getActualWeapon().setLastTargetSquare(prevTargets.get(prevTargets.size() - 1).getPosition());
            shooter.getActualWeapon().setLastDirection(GameMap.getDirection(shooter.getPosition(), shooter.getActualWeapon().getLastTarget().getPosition()));
        }
    }

    /**
     *
     * @param effect
     * @param p
     * @return
     */
    @Override
    public boolean checkEffect(MovementEffect effect, Player p) {
        return false;
    }

    /**
     * control if the effect can be apply, if it has possible target to shoot
     * @param effect
     * @param p
     * @return
     */
    @Override
    public boolean checkEffect(SimpleEffect effect, Player p) {
        return searchTarget(p)==null;
    }

    /**
     * handle square damage effect execution in collaboration with the server controller
     * @param h
     * @return
     */
    @Override
    public ServerGameMessage handle(EffectHandler h) {
        return h.handle(this);
    }

    /**
     * handle square damage effect execution after the target selection in collaboration with the server controller
     * @param h
     * @param targetList
     * @param model
     * @return
     */
    @Override
    public ServerGameMessage handleTargetSelection(EffectHandler h, List<Target> targetList, Game model) {
        List<Target> toApplyEffect = new ArrayList<>();
        for (Target t : model.getMap().getAllSquares()) {
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
        SquareDamageEffect that = (SquareDamageEffect) o;
        return damage == that.damage &&
                marks == that.marks &&
                lastTargetSquare == that.lastTargetSquare &&
                sameDirection == that.sameDirection;
    }

    /**
     * used by equals method
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(damage, marks, lastTargetSquare, sameDirection);
    }


}
