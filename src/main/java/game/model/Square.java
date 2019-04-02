package game.model;

import java.util.ArrayList;
import java.util.List;

public class Square {
    private Color color;
    private Edge[] edges;
    private boolean respawn;
    private List<Player> players;
    private List<CardWeapon> weapons;
    private CardAmmo cardAmmo;
    private int x;
    private int y;
    private Map m;

    public Square(Color color,  boolean respawn, int x, int y, Map m, Edge[] edges) {
        this.color = color;
        this.edges = edges;
        this.respawn = respawn;
        this.x = x;
        this.y = y;
        this.m = m;
        players = new ArrayList<>();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Edge[] getEdges() {
        return edges;
    }

    public Edge getEdge(Direction d)
    {
        switch (d){
            case UP:
                return edges[0];
            case RIGHT:
                return edges[1];
            case DOWN:
                return edges[2];
            default:
                return edges[3];
        }
    }


    public void setEdges(Edge[] edges) {
        this.edges = edges;
    }

    /**
     * Get the next Square in the indicate Direction
     * @param d
     * @return the next Square or null if it doesn't exist in the Map
     */
    public Square getNextSquare(Direction d)
    {
        switch (d){
            case UP:
                if (y==0)
                    return null;
                return m.getGrid()[x-1][y];
            case DOWN:
                if (y==m.getDimY())
                    return null;
                return m.getGrid()[x+1][y];
            case RIGHT:
                if (x==m.getDimX())
                    return null;
                return m.getGrid()[x][y+1];
            default:
                if (x==0)
                    return null;
                return m.getGrid()[x][y-1];

        }
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

    public void addPlayer(Player p) {
        players.add(p);
        p.setPosition(this);
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
