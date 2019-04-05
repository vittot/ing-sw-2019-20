package game.model;

public class Effect {
    private int damage;
    private int marks;
    private int minEnemy; //minimum number of enemies to hit
    private int maxEnemy; //maximum number of enemies to hit
    private boolean moveEnemy; //if the enemy has to be moved
    private boolean moveShooter; //if the player has to be moved
    private int minMove; //minimum number of movement of the shooter/enemy
    private int maxMove; //maximum number of movement of the shooter/enemy
    private int visibility; //0=enemy has to be visible, 1=enemy has to be not visible, 2=enemy can be visible or not
    private int visibilityAfter; //if the enemy must be visible after the movement effect
    private int minDist;
    private int maxDist;
    private Player shooter;
    private int cardDir; //{0,1,2,3} identify a direction for the effect
    private boolean chainTarget; //if the target should be found from the target of the last effect (eg: T.H.O.R.)
    private boolean prevTarget; //if the target has to be the last hit by the weapon
    private boolean chainMove; //if the target has to be moved in the position of the last target (eg. Vortex Cannon)
    private int differentTarget; // 0 false 1 different from last one 2 true
    private boolean sameDirection; //if the target has to be on same direction of the last target
    private boolean beforeBase; //if the effect is not base effect but can be used before the base effect
    private Target tipeOfTardet;

    public Effect(int damage, int marks, int minEnemy, int maxEnemy, boolean moveEnemy, boolean moveShooter, int minMove, int maxMove, int visibility, int visibilityAfter, int minDist, int maxDist, boolean chainTarget, boolean prevTarget, boolean chainMove, boolean differentTarget, boolean sameDirection, boolean beforeBase, Target tipeOfTardet) {
        this.damage = damage;
        this.marks = marks;
        this.minEnemy = minEnemy;
        this.maxEnemy = maxEnemy;
        this.moveEnemy = moveEnemy;
        this.moveShooter = moveShooter;
        this.minMove = minMove;
        this.maxMove = maxMove;
        this.visibility = visibility;
        this.visibilityAfter = visibilityAfter;
        this.minDist = minDist;
        this.maxDist = maxDist;
        this.chainTarget = chainTarget;
        this.prevTarget = prevTarget;
        this.chainMove = chainMove;
        this.differentTarget = differentTarget;
        this.sameDirection = sameDirection;
        this.beforeBase = beforeBase;
        this.tipeOfTardet = tipeOfTardet;
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

    public int getMinEnemy() {
        return minEnemy;
    }

    public void setMinEnemy(int minEnemy) {
        this.minEnemy = minEnemy;
    }

    public int getMaxEnemy() {
        return maxEnemy;
    }

    public void setMaxEnemy(int maxEnemy) {
        this.maxEnemy = maxEnemy;
    }

    public boolean isMoveEnemy() {
        return moveEnemy;
    }

    public void setMoveEnemy(boolean moveEnemy) {
        this.moveEnemy = moveEnemy;
    }

    public boolean isMoveShooter() {
        return moveShooter;
    }

    public void setMoveShooter(boolean moveShooter) {
        this.moveShooter = moveShooter;
    }

    public int getMinMove() {
        return minMove;
    }

    public void setMinMove(int minMove) {
        this.minMove = minMove;
    }

    public int getMaxMove() {
        return maxMove;
    }

    public void setMaxMove(int maxMove) {
        this.maxMove = maxMove;
    }

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public boolean isVisibilityAfter() {
        return visibilityAfter;
    }

    public void setVisibilityAfter(boolean visibilityAfter) {
        this.visibilityAfter = visibilityAfter;
    }

    public int getMinDist() {
        return minDist;
    }

    public void setMinDist(int minDist) {
        this.minDist = minDist;
    }

    public int getMaxDist() {
        return maxDist;
    }

    public void setMaxDist(int maxDist) {
        this.maxDist = maxDist;
    }

    public Player getShooter() {
        return shooter;
    }

    public void setShooter(Player shooter) {
        this.shooter = shooter;
    }

    public int getCardDir() {
        return cardDir;
    }

    public void setCardDir(int cardDir) {
        this.cardDir = cardDir;
    }

    public boolean isChainTarget() {
        return chainTarget;
    }

    public void setChainTarget(boolean chainTarget) {
        this.chainTarget = chainTarget;
    }

    public boolean isPrevTarget() {
        return prevTarget;
    }

    public void setPrevTarget(boolean prevTarget) {
        this.prevTarget = prevTarget;
    }

    public boolean isChainMove() {
        return chainMove;
    }

    public void setChainMove(boolean chainMove) {
        this.chainMove = chainMove;
    }

    public boolean isDifferentTarget() {
        return differentTarget;
    }

    public void setDifferentTarget(boolean differentTarget) {
        this.differentTarget = differentTarget;
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
}
