package game.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Square implements Target{
    private MapColor color;
    private Edge[] edges;
    private boolean respawn;
    private List<Player> players;
    private List<CardWeapon> weapons;
    private CardAmmo cardAmmo;
    //Convention: the map starts with (0,0) the left upper corner and ends with (map.dimY-1,map.dimX-1) at the lower right corner
    private int x;
    private int y;
    private Map map;

    public Square(MapColor color, boolean respawn, int x, int y, Map map, Edge[] edges) {
        this.color = color;
        this.edges = edges;
        this.respawn = respawn;
        this.x = x;
        this.y = y;
        this.map = map;
        players = new ArrayList<>();
        weapons = new ArrayList<>();
    }


    public Map getMap() { return map; }

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

    /**
     * Return the square's edge in the given Direction
     * @param d
     * @return
     */
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
                return map.getGrid()[y-1][x];
            case DOWN:
                if (y== map.getDimY())
                    return null;
                return map.getGrid()[y+1][x];
            case RIGHT:
                if (x== map.getDimX())
                    return null;
                return map.getGrid()[y][x+1];
            default:
                if (x==0)
                    return null;
                return map.getGrid()[y][x-1];

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

    /**
     * Add a Player to this Square
     * @param p
     */
    public void addPlayer(Player p) {
        players.add(p);
        p.setPosition(this);
    }

    public List<CardWeapon> getWeapons() {
        return weapons;
    }

    public void addWeapon(List<CardWeapon> weapons)
    {
        this.weapons.addAll(weapons);
    }

    public CardAmmo getCardAmmo() {
        return cardAmmo;
    }

    public void setCardAmmo(CardAmmo cardAmmo) {
        this.cardAmmo = cardAmmo;
    }

    /**
     * Return the visible targets from this Square, in the indicated distance range
     * @param minDist minimum allowed distance for targets
     * @param maxDist maximum allowed distance for targets
     * @return players available as targets
     */
    public List<Player> getVisiblePlayers(int minDist, int maxDist)
    {
        Square next;
        MapColor c = this.getColor();
        List<Square> result = map.getRoom(c).stream().filter(s2 -> Map.distanceBtwSquares(this,s2)<=maxDist && Map.distanceBtwSquares(this,s2)>=minDist).collect(Collectors.toList());
        for(Direction d : Direction.values())
        {
            if(this.getEdge(d) == Edge.DOOR)
            {
                next = this.getNextSquare(d);
                result.addAll(map.getRoom(next.getColor()).stream().filter(s2 -> Map.distanceBtwSquares(this,s2)<=maxDist && Map.distanceBtwSquares(this,s2)>=minDist).collect(Collectors.toList()));
            }

        }
        return result.stream().flatMap(square -> square.getPlayers().stream()).collect(Collectors.toList());
    }

    public List<Room> getVisibileRooms()
    {
        List<Room> visRooms = new ArrayList<>();
        for(Direction d : Direction.values())
        {
            if(this.getEdge(d) == Edge.DOOR)
            {
                visRoooms.add(this.getNextSquare(d);
                result.addAll(map.getRoom(next.getColor()).stream().filter(s2 -> Map.distanceBtwSquares(this,s2)<=maxDist && Map.distanceBtwSquares(this,s2)>=minDist).collect(Collectors.toList()));
            }

        }
    }

    /**
     * Get all targets not visible frome this Square, in the indicated distance range
     * @param minDist minimum allowed distance for targets
     * @param maxDist maximum allowed distance for targets
     * @return
     */
    public List<Player> getInvisiblePlayers(int minDist, int maxDist)
    {
        List<Player> visibleTargets = getVisiblePlayers(minDist,maxDist);
        return map.getAllPlayers().stream().filter(p -> !visibleTargets.contains(p) && Map.distanceBtwSquares(this,p.getPosition())<=maxDist && Map.distanceBtwSquares(this,p.getPosition())>=minDist).collect(Collectors.toList());
    }


    /**
     *
     * @param d
     * @return
     */
    public List<Square> getAdiacentRoomSquares(Direction d){
        Square tmp;
        if(this.getEdge(d)== Edge.DOOR) {
            tmp = this.getNextSquare(d);
            return map.getRoom(tmp.getColor());
        }
        else
            return null;
    }

    public List<List<Square>> getVisibleSquares(){
        List<List<Square>> result=new ArrayList<>();
        List<Square> tmp = new ArrayList<>();
        tmp= map.getRoom(this.getColor());
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

    @Override
    public void addDamage(Player shooter, int damage) {
        players.stream().forEach( p -> p.addDamage(shooter,damage));
    }

    @Override
    public void addThisTurnMarks(Player shooter, int marks) {
        players.stream().forEach(p -> p.addThisTurnMarks(shooter, marks));
    }

    @Override
    public void move(int numSquare, Direction dir) {
        players.stream().forEach(p -> p.move(numSquare, dir));
    }

    /*
    public List<List<Target>> getSquaresInRange(int minDist, int maxDist){
        List<List<Target>> result=new ArrayList<>();
        List<Target> tmp=new ArrayList<>();
        int sum;
        for(int x=0;x<map.getDimX();x++){
            for(int y=0;y<map.getDimY();y++){
                sum=Math.abs(this.getX()-map.getGrid()[x][y].getX())+Math.abs(this.getY()-map.getGrid()[x][y].getY());
                if(sum>=minDist && sum<=maxDist){
                    tmp.add(map.getGrid()[x][y]);
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
    }*/

}
