package game.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SquareTest {

    private GameMap map;

    @BeforeEach
    public void before()  {
        map = new GameMap(1,2,3);
        Square[][] grid = new Square[3][2];
        Edge[] edges1 = new Edge[]{Edge.WALL,Edge.OPEN,Edge.DOOR,Edge.WALL};
        Edge[] edges2 = new Edge[]{Edge.DOOR,Edge.WALL,Edge.OPEN,Edge.WALL};
        Edge[] edges3 = new Edge[]{Edge.OPEN,Edge.WALL,Edge.WALL,Edge.WALL};
        Edge[] edges4 = new Edge[]{Edge.WALL,Edge.WALL,Edge.DOOR,Edge.OPEN};
        Edge[] edges5 = new Edge[]{Edge.DOOR,Edge.WALL,Edge.WALL,Edge.WALL};

        grid[0][0] = new Square(MapColor.BLUE, false, 0, 0, map, edges1);
        grid[1][0] = new Square(MapColor.RED, false, 0, 1, map, edges2);
        grid[2][0] = new Square(MapColor.RED, true, 0, 2, map, edges3);
        grid[0][1] = new Square(MapColor.BLUE, true, 1, 0, map, edges4);
        grid[1][1] = new Square(MapColor.YELLOW, true, 1, 1, map, edges5);

        map.setGrid(grid);
    }

    /**
     * Return all the squares in the map attainable following the four cardinal directions (with a specific distance range)
     */
    @Test
    void getSquaresInDirections(){
        List<Square> result = map.getGrid()[0][0].getSquaresInDirections(1,2);
        List<Square> checkResult = new ArrayList<>();
        checkResult.add(map.getGrid()[0][1]);
        checkResult.add(map.getGrid()[1][0]);
        checkResult.add(map.getGrid()[2][0]);
        assertTrue(checkResult.containsAll(result) && result.containsAll(checkResult));
    }

    /**
     * Return all the squares placed in the adiacent room of the current position following a specified direction
     */
    @Test
    void getAdiacentRoomsSquares(){
        List<Square> result = map.getGrid()[1][0].getAdiacentRoomSquares(Direction.UP);
        List<Square> checkResult = new ArrayList<>();
        checkResult.add(map.getGrid()[0][0]);
        checkResult.add(map.getGrid()[0][1]);
        assertTrue(checkResult.containsAll(result) && result.containsAll(checkResult));
    }
}