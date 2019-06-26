package game.model.effects;

import game.controller.EffectHandler;
import game.controller.commands.ServerMessage;
import game.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class SquareDamageEffect extends SimpleEffect {
    private int damage;
    private int marks;
    private boolean lastTargetSquare; //if true indicate to search target from the last target position
    private boolean sameDirection;

    public SquareDamageEffect(int minEnemy, int maxEnemy, int minDist, int maxDist, TargetVisibility visibility, int damage, int marks, boolean lastTargetSquare, boolean sameDirection) {
        super(minEnemy, maxEnemy, minDist, maxDist, visibility);
        this.damage = damage;
        this.marks = marks;
        this.lastTargetSquare = lastTargetSquare;
        this.sameDirection = sameDirection;
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

    public boolean isLastTargetSquare() {
        return lastTargetSquare;
    }

    public void setLastTargetSquare(boolean lastTargetSquare) {
        this.lastTargetSquare = lastTargetSquare;
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

        List<Target> retList = new ArrayList<>();
        for(Square s: targets)
            if(!s.getPlayers().isEmpty())
                retList.add(s);

        return retList;

    }

    /**
     * Apply damage and marks to the targets
     * @param shooter
     * @param targets choosen targets
     */
    public void applyEffect(Player shooter, List<Target> targets){
        List<Player> prevTargets = shooter.getActualWeapon().getPreviousTargets();
        Square s;
        for(Target t : targets)
        {
            s = (Square) t;
            t.addDamage(shooter,damage);
            t.addThisTurnMarks(shooter,marks);
            prevTargets.addAll(s.getPlayers());
        }
        shooter.getActualWeapon().setLastTargetSquare(prevTargets.get(prevTargets.size()-1).getPosition());
        shooter.getActualWeapon().setLastDirection(GameMap.getDirection(shooter.getPosition(),shooter.getActualWeapon().getLastTarget().getPosition()));
    }

    @Override
    public boolean checkEffect(MovementEffect effect, Player p) {
        return false;
    }

    @Override
    public boolean checkEffect(SimpleEffect effect, Player p) {
        return searchTarget(p)==null;
    }

    @Override
    public ServerMessage handle(EffectHandler h) {
        return h.handle(this);
    }

    @Override
    public ServerMessage handleTargetSelection(EffectHandler h, List<Target> targetList, Game model) {
        List<Target> toApplyEffect = new ArrayList<>();
        for (Target t : model.getMap().getAllSquares()) {
            if (targetList.contains(t))
                toApplyEffect.add(t);
        }
        return h.handleTarget(this, toApplyEffect);
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(damage, marks, lastTargetSquare, sameDirection);
    }


}
