package game.model;

import java.util.ArrayList;
import java.util.List;

public class Square implements Target{
    private MapColor color;
    private Edge[] edges;
    private boolean respawn;
    private List<Player> players;
    private List<CardWeapon> weapons;
    private CardAmmo cardAmmo;
    private int x;
    private int y;
    private Map m;

    public Square(MapColor color,  boolean respawn, int x, int y, Map m, Edge[] edges) {
        this.color = color;
        this.edges = edges;
        this.respawn = respawn;
        this.x = x;
        this.y = y;
        this.m = m;
        players = new ArrayList<>();
    }

    public MapColor getColor() {
        return color;
    }

    public void setColor(MapColor color) {
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

    public List<Square> getSameColorSquares(){
        List<Square> sameMapColor=new ArrayList<>();
        for(int x=0;x<m.getDimX();x++){
            for(int y=0;y<m.getDimY();y++){
                if(this.color==m.getGrid()[x][y].getColor()){
                    sameMapColor.add(m.getGrid()[x][y]);
                }
            }
        }
        return sameMapColor;
    }

    public List<Square> getAdiacentRoomSquares(Direction d){
        Square tmp;
        if(this.getEdge(d)== Edge.DOOR) {
            tmp = this.getNextSquare(d);
            return tmp.getSameColorSquares();
        }
        else
            return null;
    }

    public List<List<Square>> getVisibleSquares(){
        List<List<Square>> result=new ArrayList<>();
        List<Square> tmp = new ArrayList<>();
        tmp=this.getSameColorSquares();
        result.add(tmp);
        if(this.getAdiacentRoomSquares(Direction.UP)!=null){
            result.add((ArrayList)this.getAdiacentRoomSquares(Direction.UP));
        }
        if (this.getAdiacentRoomSquares(Direction.RIGHT)!=null){
            result.add((ArrayList)this.getAdiacentRoomSquares(Direction.RIGHT));
        }
        if (this.getAdiacentRoomSquares(Direction.DOWN)!=null){
            result.add((ArrayList)this.getAdiacentRoomSquares(Direction.DOWN));
        }
        if (this.getAdiacentRoomSquares(Direction.LEFT)!=null){
            result.add((ArrayList)this.getAdiacentRoomSquares(Direction.LEFT));
        }
        return result;
    }

    public List<List<Target>> getSquaresInRange(int minDist, int maxDist){
        List<List<Target>> result=new ArrayList<>();
        List<Target> tmp=new ArrayList<>();
        int sum;
        for(int x=0;x<m.getDimX();x++){
            for(int y=0;y<m.getDimY();y++){
                sum=Math.abs(this.getX()-m.getGrid()[x][y].getX())+Math.abs(this.getY()-m.getGrid()[x][y].getY());
                if(sum>=minDist && sum<=maxDist){
                    tmp.add(m.getGrid()[x][y]);
                    result.add(tmp);
                    tmp.clear();
                }
            }
        }
        return result;
    }

    public List<List<Target>> getSquaresInRange(int minDist, int maxDist, int lastDirection){
        List<List<Target>> result=new ArrayList<>();
        List<Target> tmp=new ArrayList<>();
        Square actual,next;
        int sum;
        Direction d=Direction.getDirection(lastDirection);
        actual=this;
        for(int i=0;i<maxDist;i++){
            next=actual.getNextSquare(d);
            sum=Math.abs(this.getX()-next.getX())+Math.abs(this.getY()-next.getY());
            if(sum>=minDist && sum<=maxDist){
                tmp.add(next);
                result.add(tmp);
                tmp.clear();
            }
            actual=next;
        }
        return result;
    }

}
