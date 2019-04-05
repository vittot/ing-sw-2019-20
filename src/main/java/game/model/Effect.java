package game.model;

public abstract class Effect {
    private int minEnemy;
    private int maxEnemy;
    private int minDist;
    private int maxDist;
    private int visibility;

    public Effect(int minEnemy, int maxEnemy, int minDist, int maxDist, int visibility) {
        this.minEnemy = minEnemy;
        this.maxEnemy = maxEnemy;
        this.minDist = minDist;
        this.maxDist = maxDist;
        this.visibility = visibility;
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

    public int getVisibility() {
        return visibility;
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }
}