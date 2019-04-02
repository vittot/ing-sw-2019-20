package game.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class MapTest {

    private Map map;

    @BeforeEach
    public void before()  {
        map = new Map(1,3,1);
        Square[][] grid = new Square[3][1];
        Edge[] edges1 = new Edge[]{Edge.WALL,Edge.WALL,Edge.OPEN,Edge.WALL};
        Edge[] edges2 = new Edge[]{Edge.OPEN,Edge.WALL,Edge.DOOR,Edge.WALL};
        Edge[] edges3 = new Edge[]{Edge.DOOR,Edge.WALL,Edge.WALL,Edge.WALL};
        grid[0][0] = new Square(MapColor.BLUE, false, 0, 0, map, edges1);
        grid[1][0] = new Square(MapColor.BLUE, false, 1, 0, map, edges2);
        grid[2][0] = new Square(MapColor.RED, false, 2, 0, map, edges3);
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
        Player p1 = new Player(1);
        Player p2 = new Player(2);
        Player p3 = new Player(3);

        map.getGrid()[0][0].addPlayer(p1);
        map.getGrid()[1][0].addPlayer(p2);
        map.getGrid()[2][0].addPlayer(p3);

        List<Player> visible = new ArrayList<>();
        visible.add(p1);
        visible.add(p3);

        List<Player> visibleRes = map.getVisibleTargets(map.getGrid()[1][0],20,1);

        assertEquals(visible,visibleRes);


    }

}