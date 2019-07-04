package game.model.effects;

import game.controller.EffectHandler;
import game.controller.commands.ServerGameMessage;
import game.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * class that permit to apply area damages to players one square away from the shooter
 */
public class AreaDamageEffect extends SimpleEffect {
    private int damage; /** attribute tat contains the damages to apply */
    private int marks; /** attribute tat contains the marks to apply */
    private int maxEnemyPerSquare; /** attribute tat contains the number of enemy toshoot per square */

    /**
     * construct an area damage effect with the correct parameter
     * @param minEnemy
     * @param maxEnemy
     * @param minDist
     * @param maxDist
     * @param visibility
     * @param damage
     * @param marks
     * @param maxEnemyPerSquare
     */
    public AreaDamageEffect(int minEnemy, int maxEnemy, int minDist, int maxDist, TargetVisibility visibility, int damage, int marks, int maxEnemyPerSquare) {
        super( minEnemy, maxEnemy, minDist, maxDist, visibility);
        this.damage = damage;
        this.marks = marks;
        this.maxEnemyPerSquare = maxEnemyPerSquare;
    }

    /**
     * return maxEnemyPerSquare attribute
     * @return maxEnemyPerSquare
     */
    public int getMaxEnemyPerSquare() {
        return maxEnemyPerSquare;
    }

    /**
     * return damage attribute
     * @return damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * set damage attribute
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
     * set marks attribute
     * @param marks
     */
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
        List<Square> shooted = new ArrayList<>();
        int n1 = 0, n2 = 0, n3 = 0;
        Player p;
        boolean correct;
        for(Target t : targets)
        {
            correct = false;
            p = (Player) t;
            if(shooted.size() < 3 && !shooted.contains(p.getPosition()))
                shooted.add(p.getPosition());
            switch(shooted.indexOf(p.getPosition())){
                case 0:
                    n1++;
                    if(n1<=maxEnemyPerSquare)
                        correct = true;
                    break;
                case 1:
                    n2++;
                    if(n2<=maxEnemyPerSquare)
                        correct = true;
                    break;
                case 2:
                    n3++;
                    if(n3<=maxEnemyPerSquare)
                        correct = true;
                    break;
                default:
                    break;
            }
            if(correct) {
                t.addDamage(shooter, damage);
                t.addThisTurnMarks(shooter, marks);
            }
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

    /**
     * used in collaboration with server controller to proceed with the next step to manage the effect
     * @param h
     * @return server message
     */
    @Override
    public ServerGameMessage handle(EffectHandler h) {
        return h.handle(this);
    }

    /**
     * used in collaboration with server controller to proceed with the next step to manage the target selection of the effect
     * @param h
     * @param targetList
     * @param model
     * @return server message
     */
    @Override
    public ServerGameMessage handleTargetSelection(EffectHandler h, List<Target> targetList, Game model) {
        return h.handleTarget(this, targetList);
    }

    /**
     * control if two effects are equals
     * @param o
     * @return true/false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AreaDamageEffect that = (AreaDamageEffect) o;
        return damage == that.damage &&
                marks == that.marks &&
                maxEnemyPerSquare == that.maxEnemyPerSquare;
    }

    /**
     * used by equals method
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(damage, marks, maxEnemyPerSquare);
    }
}
