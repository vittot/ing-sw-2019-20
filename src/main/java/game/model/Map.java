package game.model;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Map {
    private int id;
    private Square[][] grid;
    private int dimX, dimY;


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

    public Map(int id, int dim1,int dim2)
    {
        this.id = id;
        this.dimX = dim1;
        this.dimY = dim2;
    }

    public List<Square> getRoom(MapColor c)
    {
        List<Square> room = new ArrayList<>();
        int i,j;
        for(i=0; i< dimX; i++)
        {
            for(j=0; j< dimY; j++)
                if(grid[i][j].getColor() == c)
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
        List<Square> result = this.getRoom(c).stream().filter((s2) -> Map.distanceBtwSquares(s,s2)<=maxDist && Map.distanceBtwSquares(s,s2)>=minDist).collect(Collectors.toList());
        for(Direction d : Direction.values())
        {
            if(s.getEdge(d) == Edge.DOOR)
            {
                next = s.getNextSquare(d);
                result.addAll(this.getRoom(next.getColor()).stream().filter((s2) -> Map.distanceBtwSquares(s,s2)<=maxDist && Map.distanceBtwSquares(s,s2)>=minDist).collect(Collectors.toList()));
            }

        }

        return result.stream().flatMap(square -> square.getPlayers().stream()).collect(Collectors.toList());
    }
}
