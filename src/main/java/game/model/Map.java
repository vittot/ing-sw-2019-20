package game.model;

import game.model.exceptions.MapOutOfLimitException;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;
import java.util.stream.Collectors;

public class Map implements Serializable {
    private int id;
    private Square[][] grid;
    private int dimX; //number of cols
    private int dimY; //number of rows
    public static final int MAX_DIST = 12;
    public static final int WEAPON_PER_SQUARE = 3;
    private List<Room> rooms;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Square[][] getGrid() {
        return grid;
    }

    public void setGrid(Square[][] grid) {
        this.grid = grid;
    }

    public int getDimX() {
        return dimX;
    }

    public void setDimX(int dimX) {
        this.dimX = dimX;
    }

    public int getDimY() {
        return dimY;
    }

    public void setDimY(int dimY) {
        this.dimY = dimY;
    }

    public Map(int id, int dimX,int dimY)
    {
        this.id = id;
        this.dimX = dimX;
        this.dimY = dimY;
        rooms = new ArrayList<>();
        for(MapColor c : MapColor.values())
            rooms.add(new Room(c,this));
    }

    /**
     * Return all spawnpoints of the Map
     * @return
     */
    public List<Square> getSpawnpoints()
    {
        return Arrays.stream(grid).flatMap(Arrays::stream).filter(s->s!=null && s.isRespawn()).collect(Collectors.toList());
    }

    /**
     * Return all map squares which are not spawnpoints
     * @return
     */
    public List<Square> getNormalSquares()
    {
        return Arrays.stream(grid).flatMap(Arrays::stream).filter(s->s!=null && !s.isRespawn()).collect(Collectors.toList());
    }

    /**
     * Get all Map squares as List<Square>
     * @return
     */
    public List<Square> getAllSquares()
    {
        //Objects::nonNull is equal to s->s!=null
        return Arrays.stream(grid).flatMap(Arrays::stream).filter(Objects::nonNull).collect(Collectors.toList());
    }

    /**
     * Get all Rooms of the Map
     * @return
     */
    public List<Room> getAllRooms()
    {
        return rooms;
    }

    /**
     * Return the Room of the given color
     * @param c
     * @return
     */
    public Room getRoomByColor(MapColor c)
    {
        for(Room r : rooms)
            if(r.getColor() == c)
                return r;
        return null;
    }

    /**
     * Get the Square in the given position
     * @param x number of col (from zero)
     * @param y number of row (from zero)
     * @return the Square
     * @throws MapOutOfLimitException if the given coordinates go outside of the Map
     */
    public Square getSquare(int x, int y) throws MapOutOfLimitException
    {
        if(x<0 || y<0 || x>=dimX || y>=dimY || grid[y][x] == null)
            throw new MapOutOfLimitException();
        return grid[y][x];
    }

    /**
     * Return the squares of the room of the indicated MapColor
     * @param c MapColor of the room
     * @return Squares of the room
     */
    public List<Square> getRoomSquares(MapColor c)
    {
        List<Square> room = new ArrayList<>();
        int i;
        int j;
        for(i=0; i< dimY; i++)
        {
            for(j=0; j< dimX; j++)
                if(grid[i][j]!=null && grid[i][j].getColor() == c)
                    room.add(grid[i][j]);
        }
        return room;

    }

    /**
     * Calculate the distance between two Squares, considering walls
     * @param s1
     * @param s2
     * @return
     */
    public static int distanceBtwSquares(Square s1,Square s2)
    {
        //return Math.abs(s1.getX() - s2.getX()) + Math.abs(s1.getY() - s2.getY());
        return _distanceBtwSquares(s1,s2,s1);

    }

    /**
     * Service method used in the calculation of the minimum distance between two squares
     * @param s1
     * @param s2
     * @param originSquare origin square in the calculus path (avoid to come back)
     * @return
     */
    private static int _distanceBtwSquares(Square s1, Square s2, Square originSquare)
    {
        if(s1 == s2)
            return 0;

        int[] dists = new int[4];
        for(int i=0;i<4;i++)
            dists[i] = MAX_DIST + 1;

        for(Direction d : Direction.values())
            if((s1.getEdge(d) == Edge.OPEN || s1.getEdge(d) == Edge.DOOR) && s1.getNextSquare(d) != null && s1.getNextSquare(d) != originSquare)
                dists[d.ordinal()] = 1 + _distanceBtwSquares(s1.getNextSquare(d),s2,s1);

        return Arrays.stream(dists).min().getAsInt();
    }

    /**
     * Get a List of all Players on the Map
     * @return
     */
    public List<Player> getAllPlayers()
    {
        return this.getAllSquares().stream().flatMap(s -> s.getPlayers().stream()).collect(Collectors.toList());
    }

    /**
     * Get the respawn point of a given color
     * @param c the color
     * @return the Square with the respawn color
     */
    public Square respawnColor(MapColor c){
        List<Square> room = getRoomSquares(c);
        for(int i=0;i<room.size();i++){
            if(room.get(i).isRespawn())
                return room.get(i);
        }
        return null;
    }
}
