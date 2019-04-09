package game.model;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Map {
    private int id;
    private Square[][] grid;
    private int dimX;
    private int dimY;
    public static final int MAX_DIST = 12;
    public static final int WEAPON_PER_SQUARE = 3;



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
     * Return the room of the indicated MapColor
     * @param c Color of the room
     * @return Squares of the room
     */
    public List<Square> getRoom(MapColor c)
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
     * Calculate the distance between two Squares 
     * @param s1
     * @param s2
     * @return
     */
    static int distanceBtwSquares(Square s1,Square s2)
    {
        return Math.abs(s1.getX() - s2.getX()) + Math.abs(s1.getY() - s2.getY());
    }

    /**
     * Return the visible target from the indicated position in the indicated distance range
     * @param s position
     * @param maxDist maximum allowed distance for targets
     * @param minDist minimum allowed distance for targets
     * @return players available as targets
     */
    public List<Player> getVisibleTargets(Square s, int maxDist, int minDist)
    {
        Square next;
        MapColor c = s.getColor();
        List<Square> result = this.getRoom(c).stream().filter(s2 -> Map.distanceBtwSquares(s,s2)<=maxDist && Map.distanceBtwSquares(s,s2)>=minDist).collect(Collectors.toList());
        for(Direction d : Direction.values())
        {
            if(s.getEdge(d) == Edge.DOOR)
            {
                next = s.getNextSquare(d);
                result.addAll(this.getRoom(next.getColor()).stream().filter(s2 -> Map.distanceBtwSquares(s,s2)<=maxDist && Map.distanceBtwSquares(s,s2)>=minDist).collect(Collectors.toList()));
            }

        }
        return result.stream().flatMap(square -> square.getPlayers().stream()).collect(Collectors.toList());
    }

    /**
     * Get the respawn point of a given color
     * @param c the color
     * @return the Square with the respawn color
     */
    public Square respawnColor(MapColor c){
        List<Square> room = getRoom(c);
        for(int i=0;i<room.size();i++){
            if(room.get(i).isRespawn())
                return room.get(i);
        }
        return null;
    }
}
