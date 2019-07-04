package game.model.effects;

import game.controller.EffectHandler;
import game.controller.commands.ServerGameMessage;
import game.model.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * simple effect that manage the possibility of apply movements of players
 */
public class MovementEffect extends SimpleEffect {
    private boolean moveShooter; /** true if the shooter has to be moved, false if the enemy has to be moved */
    private TargetVisibility visibilityAfter; /** attribute that indicate if the victim of the movement has to be visible from the shooter after the application of the effect */
    private boolean myPos; /** true if the target has to be moved into the position of the shooter */
    private boolean chainMove; /** if the target has to be moved in the position of the last target (eg. Vortex Cannon) */
    private boolean lastTarget; /** true if has to be selected a target from the last shot, false otherwise */
    private boolean sameDirection; /** true if the next target has to be select in the same direction of the previous */
    private DifferentTarget differentTarget; /** attribute that indicate if the shooter can't select on of the last targets shot before */
    private int minMove; /** minimum distance of the movement to apply */
    private int maxMove; /** maximum distance of the movement to apply */
    //private List<Direction> choosenMovement;

    /**
     * construct a movement effect with the correct parameters
     * @param minEnemy
     * @param maxEnemy
     * @param minDist
     * @param maxDist
     * @param minMove
     * @param maxMove
     * @param visibility
     * @param moveShooter
     * @param visibilityAfter
     * @param myPos
     * @param chainMove
     * @param lastTarget
     * @param sameDirection
     * @param differentTarget
     */
    public MovementEffect(int minEnemy, int maxEnemy, int minDist, int maxDist, int minMove, int maxMove, TargetVisibility visibility, boolean moveShooter, TargetVisibility visibilityAfter, boolean myPos, boolean chainMove, boolean lastTarget, boolean sameDirection,DifferentTarget differentTarget) {
        super(minEnemy, maxEnemy, minDist, maxDist, visibility);
        this.moveShooter = moveShooter;
        this.visibilityAfter = visibilityAfter;
        this.myPos = myPos;
        this.chainMove = chainMove;
        this.lastTarget = lastTarget;
        this.sameDirection = sameDirection;
        this.minMove = minMove;
        this.maxMove = maxMove;
        this.differentTarget = differentTarget;
    }

    /**
     * construct an empty movement effect
     */
    public MovementEffect() {
        super();
    }

    /**
     * return if the player to move has to be the shooter
     * @return moveShooter
     */
    public boolean isMoveShooter() {
        return moveShooter;
    }

    /**
     * set moveShooter attribute
     * @param moveShooter
     */
    public void setMoveShooter(boolean moveShooter) {
        this.moveShooter = moveShooter;
    }

    /**
     * return visibilityAfter attribute
     * @return visibilityAfter
     */
    public TargetVisibility getVisibilityAfter() {
        return visibilityAfter;
    }

    /**
     * set visibilityAfter attribute
     * @param visibilityAfter
     */
    public void setVisibilityAfter(TargetVisibility visibilityAfter) {
        this.visibilityAfter = visibilityAfter;
    }

    /**
     * return if the destination of movement has to be the shooter position
     * @return myPos
     */
    public boolean isMyPos() {
        return myPos;
    }

    /**
     * set myPos attribute
     * @param myPos
     */
    public void setMyPos(boolean myPos) {
        this.myPos = myPos;
    }

    /**
     * return chainMove attribute
     * @return chainMove
     */
    public boolean isChainMove() {
        return chainMove;
    }

    /**
     * set chainMove attribute
     * @param chainMove
     */
    public void setChainMove(boolean chainMove) {
        this.chainMove = chainMove;
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
     * locate the player who can be moved
     * @param shooter
     * @return
     */
    public List<Target> searchTarget (Player shooter) {
        List<Player> targets = new ArrayList<>();
        List<Player> prevTargets = null;
        if(shooter.getActualWeapon() != null) {
            if (shooter.getActualWeapon().getPreviousTargets() != null)
                prevTargets = shooter.getActualWeapon().getPreviousTargets();
        }
        else {
            if (shooter.getActualCardPower() != null && shooter.getActualCardPower().getLastTarget() != null)
                prevTargets = Collections.singletonList(shooter.getActualCardPower().getLastTarget());
        }
        Square shooterPos;
        if(chainMove)
            shooterPos = shooter.getActualWeapon().getLastTargetSquare();
        else
            shooterPos = shooter.getPosition();
        if(moveShooter) {
            targets.add(shooter);
        }
        else {
            if (lastTarget && prevTargets != null) {
                targets = prevTargets.stream().filter(p -> GameMap.distanceBtwSquares(shooterPos, p.getPosition()) <= maxDist && GameMap.distanceBtwSquares(shooterPos, p.getPosition()) >= minDist).collect(Collectors.toList());
            } else {
                switch (visibility) {
                    case VISIBLE:
                        targets = shooterPos.getVisiblePlayers(minDist, maxDist);
                        break;
                    case INVISIBLE:
                        targets = shooterPos.getInvisiblePlayers(minDist, maxDist);
                        break;
                    case DIRECTION:
                        targets = shooterPos.getPlayersInDirections(minDist, maxDist);
                        break;
                    default:
                        targets = shooterPos.getMap().getAllPlayers();
                }
            }
            if (differentTarget == DifferentTarget.NOTTHELAST)
                targets.remove(shooter.getActualWeapon().getLastTarget());

            else if (differentTarget == DifferentTarget.NONEOFTHEPREVIOUS)
                targets.removeAll(shooter.getActualWeapon().getPreviousTargets());

            targets.remove(shooter);
        }

        List<Target> retList = new ArrayList<>();
        for(Player p: targets){
            if(!selectPosition(p).isEmpty())
                retList.add(p);
        }
        return retList;
    }

    /**
     * locate the possible destination for the player that has to be moved
     * @param shooter
     * @return
     */
    public List<Square> selectPosition(Player shooter){
        List<Square> result = new ArrayList<>(); //it will contain the final result of the method
        Direction currDirection;
        if(myPos) {
            result.add(shooter.getGame().getCurrentTurn().getCurrentPlayer().getPosition());
            return result;
        }
        else if (chainMove) {
            result.add(shooter.getGame().getCurrentTurn().getCurrentPlayer().getActualWeapon().getLastTargetSquare());
            return result;
        }
        else{
            Square currentPosition = shooter.getPosition();
            Square tmp;
            if(sameDirection) {
                if(shooter.getGame().getCurrentTurn().getCurrentPlayer().getActualWeapon() != null)
                    currDirection = shooter.getGame().getCurrentTurn().getCurrentPlayer().getActualWeapon().getLastDirection();
                else
                    currDirection = shooter.getGame().getCurrentTurn().getCurrentPlayer().getActualCardPower().getLastDirection();
                tmp = currentPosition;
                if(minMove==0)
                    result.add(currentPosition);
                for (int i = 1; i <= maxMove && tmp!=null; i++) {
                    if(tmp.getEdge(currDirection) != Edge.WALL) {
                        tmp = tmp.getNextSquare(currDirection);
                        if (minMove <= i && maxMove >= i && tmp != null)
                            result.add(tmp);
                    }
                    else
                        i = maxMove+1;
                }
            }
            else {
                List<Square> squares = shooter.getGame().getMap().getAllSquares();
                for(Square s : squares){
                    //if(s.getX()!=currentPosition.getX() || s.getY()!=currentPosition.getY())
                    if(GameMap.distanceBtwSquares(s,currentPosition)>=minMove && GameMap.distanceBtwSquares(s,currentPosition)<=maxMove)
                        result.add(s);
                }
            }
            if(visibilityAfter==TargetVisibility.VISIBLE) {
                List <Target> notVisibleNext = new ArrayList<>();
                List<Square> visibles = shooter.getGame().getCurrentTurn().getCurrentPlayer().getPosition().getVisibleSquares(0, GameMap.MAX_DIST);
                for (Target t : result)
                    if(!visibles.contains(t)){
                        notVisibleNext.add(t);
                    }
                result.removeAll(notVisibleNext);
            }
        }
        return result;
    }

    /**
     * apply the movement of the shooter into the unique square selected from the list of possible squares generated by the searchTarget() method
     * @param player player that has to be moved
     * @param targets the first element of targets is the position where the player has to be moved
     */
    @Override
    public void applyEffect(Player player, List<Target> targets){
        if(!player.getPosition().equals(targets.get(0)))
            player.move((Square)targets.get(0));
    }

    /**
     * control if the effect can be used
     * @param effect
     * @param p
     * @return
     */
    @Override
    public boolean checkEffect(MovementEffect effect, Player p) {
        return searchTarget(p)==null;
    }

    /**
     *
     * @param effect
     * @param p
     * @return
     */
    @Override
    public boolean checkEffect(SimpleEffect effect, Player p) {
        return false;
    }

    /**
     * handle movement effect application
     * @param h
     * @return
     */
    @Override
    public ServerGameMessage handle(EffectHandler h) {
        return h.handle(this);
    }

    /**
     * handle movement effect after target selection
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

    /**
     * verify if two effects are equals
     * @param o
     * @return true/false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MovementEffect that = (MovementEffect) o;
        return moveShooter == that.moveShooter &&
                myPos == that.myPos &&
                chainMove == that.chainMove &&
                lastTarget == that.lastTarget &&
                sameDirection == that.sameDirection &&
                minMove == that.minMove &&
                maxMove == that.maxMove &&
                visibilityAfter == that.visibilityAfter &&
                differentTarget == that.differentTarget;
    }

    /**
     * used by equals method
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(moveShooter, visibilityAfter, myPos, chainMove, lastTarget, sameDirection, differentTarget, minMove, maxMove);
    }
}
