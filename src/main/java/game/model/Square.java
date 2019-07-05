package game.model;

import game.model.exceptions.MapOutOfLimitException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * class that represents a single tile in the map
 */
public class Square implements Target, Serializable {
    /** field that contains the square room color */
    private MapColor color;
    /** array that specify the 4 edges of the square between {WALL, DOOR, OPEN} (up-right-down-left from 0 to 3) */
    private Edge[] edges;
    /** field that specify if the square is a respawn square in the map */
    private boolean respawn;
    /** reference to all the players that are positioned in this square */
    private List<Player> players;
    /** list of weapons available in this tile (only in case of respawn tile) */
    private List<CardWeapon> weapons;
    /** card ammo available to grab in this tile (only if this is not a respawn tile) */
    private CardAmmo cardAmmo;
    //Convention: the map starts with (0,0) the left upper corner and ends with (map.dimY-1,map.dimX-1) at the lower right corner
    /** this is the numb of the column (from 0), the 2nd index in the grid matrix (x coordinate) */
    private int x;
    /** this is the numb of the row (from 0), the 1st idex in the grid matrix (y coordinate) */
    private int y;
    /** reference to the game */
    private GameMap map;

    /**
     * Construct an empty square
     * @param color
     * @param respawn true if it is a respawn point
     * @param x Number of column (coherent with the x attribute semantic)
     * @param y Number of row (coherent with the y attribute semantic)
     * @param map the map to which it belongs to
     * @param edges
     */
    public Square(MapColor color, boolean respawn, int x, int y, GameMap map, Edge[] edges) {
        this.color = color;
        this.edges = edges;
        this.respawn = respawn;
        this.x = x;
        this.y = y;
        this.map = map;
        players = new ArrayList<>();
        weapons = new ArrayList<>();
    }

    /**
     * construct an empty square
     */
    public Square() {
        players = new ArrayList<>();
        weapons = new ArrayList<>();
    }

    /**
     * return the reference to the game map
     * @return map
     */
    public GameMap getMap() { return map; }

    /**
     * set the map attribute
     * @param map
     */
    public void setMap(GameMap map) {
        this.map = map;
    }

    /**
     * return color attribute
     * @return color
     */
    public MapColor getColor() {
        return color;
    }

    /**
     * set the color attribute
     * @param color
     */
    public void setColor(MapColor color) {
        this.color = color;
    }

    /**
     * return the x coordinate of the square in the map grid
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * set the x coordinate of the square
     * @param x
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * return the y coordinate of the square in the map grid
     * @return
     */
    public int getY() {
        return y;
    }

    /**
     * set the y coordinate of the square
     * @param y
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * return the edges attribute
     * @return edges
     */
    public Edge[] getEdges() {
        return edges;
    }

    /**
     * set the weapons attribute
     * @param weapons
     */
    public void setWeapons(List<CardWeapon> weapons) {
        this.weapons = weapons;
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
            case LEFT:
                return edges[3];
            default:
                return null;
        }
    }

    /**
     * set the edges attribute
     * @param edges
     */
    public void setEdges(Edge[] edges) {
        this.edges = edges;
    }

    /**
     * Get the next Square in the indicate Direction
     * @param d direction to consider
     * @return the next Square or null if it doesn't exist in the GameMap
     */
    public Square getNextSquare(Direction d)
    {
        switch (d){
            case UP:
                if (y==0)
                    return null;
                return map.getGrid()[y-1][x];
            case DOWN:
                if (y== map.getDimY()-1)
                    return null;
                return map.getGrid()[y+1][x];
            case RIGHT:
                if (x== map.getDimX()-1)
                    return null;
                return map.getGrid()[y][x+1];
            default:
                if (x==0)
                    return null;
                return map.getGrid()[y][x-1];

        }
    }

    /**
     * return if the square is a respawn square
     * @return respawn
     */
    public boolean isRespawn() {
        return respawn;
    }

    /**
     * set if the square is a respawn square
     * @param respawn
     */
    public void setRespawn(boolean respawn) {
        this.respawn = respawn;
    }

    /**
     * return all the players positioned in the square
     * @return players
     */
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

    /**
     * return the available weapons to grab in this square
     * @return weapons
     */
    public List<CardWeapon> getWeapons() {
        return weapons;
    }

    /**
     * add a list of weapons available to grab in the square
     * @param weapons
     */
    public void addWeapon(List<CardWeapon> weapons)
    {
        this.weapons.addAll(weapons);
    }

    /**
     * return cardAmmo attribute
     * @return cardAmmo
     */
    public CardAmmo getCardAmmo() {
        return cardAmmo;
    }

    /**
     * set cardAmmo attribute
     * @param cardAmmo
     */
    public void setCardAmmo(CardAmmo cardAmmo) {
        this.cardAmmo = cardAmmo;
    }

    /**
     * Return the visible players from this Square, in the indicated distance range
     * @param minDist minimum allowed distance for targets
     * @param maxDist maximum allowed distance for targets
     * @return players available as targets
     */
    public List<Player> getVisiblePlayers(int minDist, int maxDist)
    {
       return getVisibleSquares(minDist,maxDist).stream().flatMap(square -> square.getPlayers().stream()).collect(Collectors.toList());
    }

    /**
     * Get all players not visible from this Square, in the indicated distance range
     * @param minDist minimum allowed distance for targets
     * @param maxDist maximum allowed distance for targets
     * @return players available as targets
     */
    public List<Player> getInvisiblePlayers(int minDist, int maxDist)
    {
        return getInvisibleSquares(minDist,maxDist).stream().flatMap(square -> square.getPlayers().stream()).collect(Collectors.toList());
    }

    /**
     * Get the Rooms visible from the current position. The current Room is excluded
     * @return
     */
    public List<Room> getVisibileRooms()
    {
        List<Room> visRooms = new ArrayList<>();
        Square next;
        for(Direction d : Direction.values())
        {
            if(this.getEdge(d) == Edge.DOOR)
            {
                next = this.getNextSquare(d);
                visRooms.add(map.getRoomByColor(next.getColor()));
            }

        }
        return visRooms;
    }



    /**
     * Get all players on a cardinal direction from this Square, in the indicated distance range
     * @param minDist
     * @param maxDist
     * @return players available as targets
     */
    public List<Player> getPlayersInDirections(int minDist, int maxDist)
    {
        return getSquaresInDirections(minDist,maxDist).stream().flatMap(square -> square.getPlayers().stream()).collect(Collectors.toList());
    }

    /**
     * Get all squares on a cardinal direction from this Square, in the indicated distance range
     * @param minDist
     * @param maxDist
     * @return players available as targets
     */
    public List<Square> getSquaresInDirections(int minDist, int maxDist)
    {
        List<Square> list = map.getAllSquares().stream().filter(s -> s.getX() == this.x || s.getY() == this.y).filter(s -> GameMap.distanceBtwSquares(this,s)<=maxDist && GameMap.distanceBtwSquares(this,s)>=minDist).collect(Collectors.toList());
        return list;
    }


    /**
     * @param d
     * @return List of Square of the adiacent Room in the indicated Direction, possibly empty
     */
    public List<Square> getAdiacentRoomSquares(Direction d){
        Square tmp;
        if(this.getEdge(d)== Edge.DOOR) {
            tmp = this.getNextSquare(d);
            return map.getRoomSquares(tmp.getColor());
        }
        else
            return new ArrayList<>();
    }

    /**
     * Return the visible squares from this Square, in the indicated distance range
     * @param minDist minimum allowed distance for targets
     * @param maxDist maximum allowed distance for targets
     * @return players available as targets
     */
    public List<Square> getVisibleSquares(int minDist, int maxDist)
    {
        Square next;
        MapColor c = this.getColor();
        List<Square> result = map.getRoomSquares(c).stream().filter(s2 -> GameMap.distanceBtwSquares(this,s2)<=maxDist && GameMap.distanceBtwSquares(this,s2)>=minDist).collect(Collectors.toList());
        for(Direction d : Direction.values())
        {
            if(this.getEdge(d) != null && this.getEdge(d) == Edge.DOOR)
            {
                next = this.getNextSquare(d);
                result.addAll(map.getRoomSquares(next.getColor()).stream().filter(s2 -> GameMap.distanceBtwSquares(this,s2)<=maxDist && GameMap.distanceBtwSquares(this,s2)>=minDist).collect(Collectors.toList()));
            }

        }
        return result;
    }

    /**
     * Get all squares not visible from this Square, in the indicated distance range
     * @param minDist minimum allowed distance for targets
     * @param maxDist maximum allowed distance for targets
     * @return players available as targets
     */
    public List<Square> getInvisibleSquares(int minDist, int maxDist)
    {
        List<Square> visibleSquares = getVisibleSquares(minDist,maxDist);
        return map.getAllSquares().stream().filter(s -> !visibleSquares.contains(s) && GameMap.distanceBtwSquares(this,s)<=maxDist && GameMap.distanceBtwSquares(this,s)>=minDist).collect(Collectors.toList());
    }

    /**
     * Return the reference to the square
     * @return
     */
    @Override
    public String returnName() {
        //TODO string that identify suares
        return null;
    }

    /**
     * For every Player in the Square add damage cause of an enemy's weapon effect
     * Marks from the same enemy are counted to calculate the damage to be applied
     * The adrenaline attribute is updated according to the total damage suffered
     * Manage deaths and rages adding new kills into the kill-board
     * @param shooter
     * @param damage
     */
    @Override
    public void addDamage(Player shooter, int damage) {
        players.stream().forEach( p -> p.addDamage(shooter,damage));
    }

    /**
     * For every Player in the Square add marks to the current turn marks, saved there to avoid to be added as damage in case of composite effects
     * They will be added to the Player's effective marks at the end of the action
     * @param shooter
     * @param marks
     */
    @Override
    public void addThisTurnMarks(Player shooter, int marks) {
        players.stream().forEach(p -> p.addThisTurnMarks(shooter, marks));
    }

    /**
     * Move all players in the Square
     * @param numSquare movement amount
     * @param dir movement Direction
     * @throws MapOutOfLimitException if the movement put any Player outside of the GameMap
     */
    @Override
    public void move(int numSquare, Direction dir) throws MapOutOfLimitException{
        List<Player> py = new ArrayList<>(players);
        for (Player p : py) {
            p.move(numSquare, dir);
        }
    }

    /**
     * Remove a Player from the list of players in this Square
     * @param player
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * compare the current square with another indicated analysing characteristic fields
     * @param o square to compare
     * @return if the two squares are equals
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return respawn == square.respawn &&
                x == square.x &&
                y == square.y &&
                color == square.color &&
                Arrays.equals(edges, square.edges);
    }

    /**
     * used by the equals method
     * @return result
     */
    @Override
    public int hashCode() {
        int result = Objects.hash(color, respawn, x, y);
        result = 31 * result + Arrays.hashCode(edges);
        return result;
    }

    /**
     * describe the object in string version
     * @return description
     */
    @Override
    public String toString() {
        return "Square{" +
                " x=" + x +
                ", y=" + y +
                '}';
    }
}
