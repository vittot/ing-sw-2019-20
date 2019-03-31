package game.model;

import java.util.List;
import java.util.ArrayList;

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

    public Map(int dim1,int dim2)
    {
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
}
