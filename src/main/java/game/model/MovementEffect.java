package game.model;

import java.util.List;

public class MovementEffect extends Effect{
    private boolean moveShooter; //true if the shoother has to to be moved, false if the enemy has to be moved
    private TargetVisibility visibilityAfter;
    private boolean myPos;
    private boolean chainMove; //if the target has to be moved in the position of the last target (eg. Vortex Cannon)
    private boolean lastTarget;
    private boolean sameDirection;
    private boolean beforeBase;

    public MovementEffect(int minEnemy, int maxEnemy, int minDist, int maxDist, TargetVisibility visibility, boolean moveShooter, TargetVisibility visibilityAfter, boolean myPos, boolean chainMove, boolean lastTarget, boolean sameDirection, boolean beforeBase) {
        super(minEnemy, maxEnemy, minDist, maxDist, visibility);
        this.moveShooter = moveShooter;
        this.visibilityAfter = visibilityAfter;
        this.myPos = myPos;
        this.chainMove = chainMove;
        this.lastTarget = lastTarget;
        this.sameDirection = sameDirection;
        this.beforeBase = beforeBase;
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

    public List<List<Target>> searchTarget(Player shooter){}

    public void applyEffect(Player shooter, List<Target>){}
}