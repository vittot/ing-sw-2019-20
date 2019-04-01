package game.model;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Map {
    private int id;
    private Square[][] grid;
    private int dim1, dim2;


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

    public int getDim1() {
        return dim1;
    }

    public void setDim1(int dim1) {
        this.dim1 = dim1;
    }

    public int getDim2() {
        return dim2;
    }

    public void setDim2(int dim2) {
        this.dim2 = dim2;
    }

    public Map(int id, int dim1,int dim2)
    {
        this.id = id;
        this.dim1 = dim1;
        this.dim2 = dim2;
    }

    public List<Square> getRoom(Color c)
    {
        List<Square> room = new ArrayList<>();
        int i,j;
        for(i=0;i<dim1;i++)
        {
            for(j=0;j<dim2;j++)
                if(grid[i][j].getColor() == c)
                    room.add(grid[i][j]);
        }
        return room;

    }

    static int distanceBtwSquares(Square s1,Square s2)
    {
        return Math.abs(s1.getX() - s2.getX()) + Math.abs(s1.getY() - s2.getY());
    }

    public List<Player> getVisibleTargets(Square s, int maxDist, int minDist)
    {
        Square next;
        Color c = s.getColor();
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
