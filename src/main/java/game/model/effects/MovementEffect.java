package game.model.effects;

import game.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class MovementEffect extends Effect{
    private boolean moveShooter; //true if the shooter has to to be moved, false if the enemy has to be moved
    private TargetVisibility visibilityAfter;
    private boolean myPos;  //true if the target has to be moved into the position of the shooter
    private boolean chainMove; //if the target has to be moved in the position of the last target (eg. Vortex Cannon)
    private boolean lastTarget;
    private boolean sameDirection;
    private DifferentTarget differentTarget;
    private boolean beforeBase;
    private int minMove;
    private int maxMove;
    //private List<Direction> choosenMovement;

    public MovementEffect(int minEnemy, int maxEnemy, int minDist, int maxDist, int minMove, int maxMove, TargetVisibility visibility, boolean moveShooter, TargetVisibility visibilityAfter, boolean myPos, boolean chainMove, boolean lastTarget, boolean sameDirection, boolean beforeBase,DifferentTarget differentTarget) {
        super(minEnemy, maxEnemy, minDist, maxDist, visibility);
        this.moveShooter = moveShooter;
        this.visibilityAfter = visibilityAfter;
        this.myPos = myPos;
        this.chainMove = chainMove;
        this.lastTarget = lastTarget;
        this.sameDirection = sameDirection;
        this.beforeBase = beforeBase;
        this.minMove = minMove;
        this.maxMove = maxMove;
        this.differentTarget = differentTarget;
    }

    public boolean isMoveShooter() {
        return moveShooter;
    }

    public void setMoveShooter(boolean moveShooter) {
        this.moveShooter = moveShooter;
    }

    public TargetVisibility getVisibilityAfter() {
        return visibilityAfter;
    }

    public void setVisibilityAfter(TargetVisibility visibilityAfter) {
        this.visibilityAfter = visibilityAfter;
    }

    public boolean isMyPos() {
        return myPos;
    }

    public void setMyPos(boolean myPos) {
        this.myPos = myPos;
    }

    public boolean isChainMove() {
        return chainMove;
    }

    public void setChainMove(boolean chainMove) {
        this.chainMove = chainMove;
    }

    public boolean isLastTarget() {
        return lastTarget;
    }

    public void setLastTarget(boolean lastTarget) {
        this.lastTarget = lastTarget;
    }

    public boolean isSameDirection() {
        return sameDirection;
    }

    public void setSameDirection(boolean sameDirection) {
        this.sameDirection = sameDirection;
    }

    public boolean isBeforeBase() {
        return beforeBase;
    }

    public void setBeforeBase(boolean beforeBase) {
        this.beforeBase = beforeBase;
    }

    /**
     * locate the player who can be moved
     * @param shooter
     * @return
     */
    public List<Target> selectMoved (Player shooter) {
        List<Player> targets = new ArrayList<>();
        List<Player> prevTargets = shooter.getActualWeapon().getPreviousTargets();
        Square shooterPos = shooter.getPosition();
        if(moveShooter) {
            targets.add(shooter);
        }
        else {
            if (lastTarget) {
                targets = prevTargets.stream().filter(p -> Map.distanceBtwSquares(shooterPos, p.getPosition()) <= maxDist && Map.distanceBtwSquares(shooterPos, p.getPosition()) >= minDist).collect(Collectors.toList());
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
        for(Player p: targets)
            retList.add(p);
        return retList;
    }

    /**
     * locate the possible destination for the player that has to be moved
     * @param shooter
     * @return
     */
    @Override
    public List<Target> searchTarget(Player shooter){
        List<Target> result = new ArrayList<>(); //it will contain the final result of the method
        if(myPos) {
            result.add(shooter.getGame().getCurrentTurn().getCurrentPlayer().getPosition());
            return result;
        }
        else if (chainMove) {
            result.add(shooter.getActualWeapon().getLastTargetSquare());
            return result;
        }
        else{
            Square currentPosition = shooter.getPosition();
            Square tmp;
            if(sameDirection) {
                Direction currDirection = shooter.getGame().getCurrentTurn().getCurrentPlayer().getActualWeapon().getLastDirection();
                tmp = currentPosition;
                for (int i = 1; i <= maxMove; i++) {
                    tmp = tmp.getNextSquare(currDirection);
                    if (minMove <= i && maxMove >= i)
                        result.add(tmp);
                }
            }
            else {
                List<Square> squares = shooter.getGame().getMap().getAllSquares();
                for(Square s : squares){
                    if(s.getX()!=currentPosition.getX() || s.getY()!=currentPosition.getY())
                        if(Map.distanceBtwSquares(s,currentPosition)>=minMove && Map.distanceBtwSquares(s,currentPosition)<=maxMove)
                            result.add(s);
                }
            }
            if(visibilityAfter==TargetVisibility.VISIBLE) {
                List<Square> visibles = currentPosition.getVisibleSquares(0, Map.MAX_DIST);
                for (Target t : result)
                    if(!visibles.contains(t)){
                        result.remove(t);
                    }
            }
        }
        return result;
    }

    /**
     * apply the movement of the shooter into the unique square selected from the list of possible squares generated by the searchTarget() method
     * @param shooter
     * @param targets
     */
    @Override
    public void applyEffect(Player shooter, List<Target> targets){
        shooter.getPosition().removePlayer(shooter);
        shooter.setPosition((Square)targets.get(0));
    }
}
