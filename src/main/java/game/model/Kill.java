package game.model;

public class Kill {
    private Player killer;
    private Player victim;
    private boolean isRage;

    public Kill(Player killer, Player victim, boolean isRage) {
        this.killer = killer;
        this.victim = victim;
        this.isRage = isRage;
    }

    public Player getKiller() {
        return killer;
    }

    public void setKiller(Player killer) {
        this.killer = killer;
    }

    public Player getVictim() {
        return victim;
    }

    public void setVictim(Player victim) {
        this.victim = victim;
    }

    public boolean isRage() {
        return isRage;
    }

    public void setRage(boolean rage) {
        isRage = rage;
    }
}
