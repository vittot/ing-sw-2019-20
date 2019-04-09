package game.model.effects;

import game.model.CardWeapon;
import game.model.Player;
import game.model.Square;
import game.model.Target;

import java.util.ArrayList;
import java.util.List;


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

    public List<List<Target>> searchTarget(Player shooter){
        List<List<Target>> result = new ArrayList<>(); //it will contain the final result of the method
        if(moveShooter) { //part of the method that control the shooter movement
            Square startingPosition = shooter.getPosition(); //the starting position of the shooter
            CardWeapon actualWeapon = shooter.getWeapons().get(shooter.getActualWeapon()); //the weapon the player is using
            ArrayList<Target> tmp = new ArrayList<>(); //temporary variable that will compose the final result
            Square actual;
            Square next;
            int sIndex;
            if (chainMove) {
                tmp.add(actualWeapon.getLastTargetSquare());
                result.add(tmp);
            }
            else {
                if (sameDirection) {
                    //result=startingPosition.getSquaresInRange(this.getMinDist(),this.getMaxDist(),actualWeapon.getLastDirection());
                } else {
                    //result=startingPosition.getSquaresInRange(this.getMinDist(),this.getMaxDist());
                }
            }
        }
        else{
            //TODO code to permit the enemy movement effects
        }
        return result;
    }

    public void applyEffect(Player shooter, List<Target> targets){
        //TODO
    }
}
