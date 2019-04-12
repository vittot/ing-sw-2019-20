package game.model.effects;

import game.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


public class PlainDamageEffect extends Effect{
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
     * Return the possible target. It will have to be verified that they respect the minEnemy - maxEnemy constraint
     * @param shooter
     * @return The List is a singleton for this type of Effect
     */
    public List<List<Target>> searchTarget(Player shooter){

        List<Player> targets;
        Square shooterPos = chainTarget ? shooter.getActualWeapon().getLastTarget().getPosition() : shooter.getPosition();
        List<Player> prevTargets = shooter.getActualWeapon().getPreviousTargets();
        if(lastTarget){
            targets = prevTargets.stream().filter(p -> Map.distanceBtwSquares(shooterPos,p.getPosition())<=maxDist && Map.distanceBtwSquares(shooterPos,p.getPosition())>=minDist).collect(Collectors.toList());
        }
        else{
            switch(visibility){
                case VISIBLE:
                    targets = shooterPos.getVisiblePlayers(minDist,maxDist);
                    break;
                case INVISIBLE:
                    targets = shooterPos.getInvisiblePlayers(minDist,maxDist);
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
