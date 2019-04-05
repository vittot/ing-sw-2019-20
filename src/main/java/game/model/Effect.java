package game.model;

public abstract class Effect {
    private int minEnemy; //minimum number of enemies to hit
    private int maxEnemy; //maximum number of enemies to hit
    private int visibility; //0=enemy has to be not visible, 1=enemy has to be visible, 2=enemy can be visible or not
    private int visibilityAfter; //0=enemy has to be not visible, 1=enemy has to be visible, 2=enemy can be visible or not (after di action)
    private int minDist; //minimum distance of the target to hit or minimum distance of movement
    private int maxDist; //maximum distance of the target to hit or minimum distance of movement

    public Effect(int minEnemy, int maxEnemy, int visibility, int visibilityAfter, int minDist, int maxDist) {
        this.minEnemy = minEnemy;
        this.maxEnemy = maxEnemy;
        this.visibility = visibility;
        this.visibilityAfter = visibilityAfter;
        this.minDist = minDist;
        this.maxDist = maxDist;
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

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public int getVisibilityAfter() {
        return visibilityAfter;
    }

    public void setVisibilityAfter(int visibilityAfter) {
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
}