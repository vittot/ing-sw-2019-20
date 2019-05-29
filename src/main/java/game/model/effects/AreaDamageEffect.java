package game.model.effects;

import game.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AreaDamageEffect extends SimpleEffect {
    private int damage;
    private int marks;
    private int maxEnemyPerSquare; //quando andrà controllato vedere Collectors.groupingBy

    public AreaDamageEffect(int minEnemy, int maxEnemy, int minDist, int maxDist, TargetVisibility visibility, int damage, int marks, int maxEnemyPerSquare) {
        super( minEnemy, maxEnemy, minDist, maxDist, visibility);
        this.damage = damage;
        this.marks = marks;
        this.maxEnemyPerSquare = maxEnemyPerSquare;
    }

    public int getMaxEnemyPerSquare() {
        return maxEnemyPerSquare;
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
     * Return the possible targets. It will have to be verified that they respect the minEnemy - maxEnemy constraint and the maxEnemyPerSquare constraint
     * @param shooter
     * @return The List is a singleton for this type of SimpleEffect
     */
    public List<Target> searchTarget(Player shooter){
        List<Player> targets;
        Square shooterPos = shooter.getPosition();
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AreaDamageEffect that = (AreaDamageEffect) o;
        return damage == that.damage &&
                marks == that.marks &&
                maxEnemyPerSquare == that.maxEnemyPerSquare;
    }

    @Override
    public int hashCode() {
        return Objects.hash(damage, marks, maxEnemyPerSquare);
    }
}
