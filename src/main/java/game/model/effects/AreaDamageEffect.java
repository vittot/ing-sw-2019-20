package game.model.effects;

import game.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class AreaDamageEffect extends Effect{
    private int damage;
    private int marks;
    private int maxEnemyPerSquare; //TODO:quando andrà controllato vedere Collectors.groupingBy

    public AreaDamageEffect(int minEnemy, int maxEnemy, int minDist, int maxDist, TargetVisibility visibility, int damage, int marks, int maxEnemyPerSquare) {
        super( minEnemy, maxEnemy, minDist, maxDist, visibility);
        this.damage = damage;
        this.marks = marks;
        this.maxEnemyPerSquare = maxEnemyPerSquare;
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
     * @return The List is a singleton for this type of Effect
     */
    public List<Target> searchTarget(Player shooter){
        List<Player> targets;
        Square shooterPos =shooter.getPosition();
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
        //return Collections.singletonList(retList);
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
