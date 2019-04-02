package game.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Before;

class MapTest {

    private Map map;

    @Before
    public void before()  {
        map = new Map(1,3,1);
        Square[][] grid = new Square[3][1];
        Edge[] edges1 = new Edge[]{Edge.WALL,Edge.WALL,Edge.OPEN,Edge.WALL};
        Edge[] edges2 = new Edge[]{Edge.OPEN,Edge.WALL,Edge.DOOR,Edge.WALL};
        Edge[] edges3 = new Edge[]{Edge.DOOR,Edge.WALL,Edge.WALL,Edge.WALL};
        grid[0][0] = new Square(Color.BLUE, false, 0, 0, map, edges1);
        grid[1][0] = new Square(Color.BLUE, false, 0, 1, map, edges2);
        grid[2][0] = new Square(Color.RED, false, 0, 2, map, edges3);
        map.setGrid(grid);
    }

    @Test
    void getRoom() {

        List<Square> blueRoom = new ArrayList<>();
        blueRoom.add(map.getGrid()[0][0]);
        blueRoom.add(map.getGrid()[1][0]);

        assertEquals(map.getRoom(Color.BLUE), blueRoom);
    }

    @Test
    void getVisibleTargets() {


    }

}