package game.model;

import java.util.List;

public class Square {
    private Color color;
    private int[] edge;
    private boolean respawn;
    private List<Player> players;
    private List<CardWeapon> weapons;
    private CardAmmo cardAmmo;

    public Square(Color c)
    {
        this.color = c;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int[] getEdge() {
        return edge;
    }

    public void setEdge(int[] edge) {
        this.edge = edge;
    }

    public boolean isRespawn() {
        return respawn;
    }

    public void setRespawn(boolean respawn) {
        this.respawn = respawn;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<CardWeapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<CardWeapon> weapons) {
        this.weapons = weapons;
    }

    public CardAmmo getCardAmmo() {
        return cardAmmo;
    }

    public void setCardAmmo(CardAmmo cardAmmo) {
        this.cardAmmo = cardAmmo;
    }
}
