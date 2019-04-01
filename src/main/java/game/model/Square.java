package game.model;

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

    //TODO manage map limits
    public Square getNextSquare(Direction d)
    {
        switch (d){
            case UP:
                return m.getGrid()[x][y-1];
            case DOWN:
                return m.getGrid()[x][y+1];
            case RIGHT:
                return m.getGrid()[x+1][y];
            default:
                return m.getGrid()[x-1][y];

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
