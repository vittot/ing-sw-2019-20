package game.model.effects;

import game.controller.EffectHandler;
import game.controller.commands.ServerGameMessage;
import game.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class PlainDamageEffect extends SimpleEffect {
    private int damage;
    private int marks;
    private boolean lastTarget; //if the target has to be among the previous targets hit by the weapon
    private DifferentTarget differentTarget; //the target could be different from the last one or from all the previous
    private boolean chainTarget; //if the target should be found from the target of the last effects (eg: T.H.O.R.)
    private boolean sameDirection; //if the target has to be on the same last direction of the last one


    public PlainDamageEffect(int minEnemy, int maxEnemy, int minDist, int maxDist, TargetVisibility visibility, int damage, int marks, boolean lastTarget, DifferentTarget differentTarget, boolean chainTarget, boolean sameDirection) {
        super(minEnemy, maxEnemy, minDist, maxDist, visibility);
        this.damage = damage;
        this.marks = marks;
        this.lastTarget = lastTarget;
        this.differentTarget = differentTarget;
        this.chainTarget = chainTarget;
        this.sameDirection = sameDirection;
    }

    public int getDamage() {return damage; }

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
                    targets = targets.stream().filter(t -> t.getPosition().getY() < lastTargPos.getY()).collect(Collectors.toList());
                    break;
                case DOWN:
                    targets = targets.stream().filter(t -> t.getPosition().getY() > lastTargPos.getY()).collect(Collectors.toList());
                    break;
                case LEFT:
                    targets = targets.stream().filter(t -> t.getPosition().getX() < lastTargPos.getX()).collect(Collectors.toList());
                    break;
                case RIGHT:
                    targets = targets.stream().filter(t -> t.getPosition().getX() > lastTargPos.getX()).collect(Collectors.toList());
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

    @Override
    public boolean checkEffect(MovementEffect effect, Player p) {
        return false;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(damage, marks, lastTarget, differentTarget, chainTarget, sameDirection);
    }

    @Override
    public boolean checkEffect(SimpleEffect effect, Player p) {
        if(searchTarget(p) == null)
            return false;
        return true;
    }

    @Override
    public ServerGameMessage handle(EffectHandler h) {
        return h.handle(this);
    }

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
