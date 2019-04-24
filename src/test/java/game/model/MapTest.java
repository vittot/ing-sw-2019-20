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
        map = new Map(1,2,3);
        Square[][] grid = new Square[3][2];
        Edge[] edges1 = new Edge[]{Edge.WALL,Edge.OPEN,Edge.OPEN,Edge.WALL};
        Edge[] edges2 = new Edge[]{Edge.OPEN,Edge.WALL,Edge.DOOR,Edge.WALL};
        Edge[] edges3 = new Edge[]{Edge.DOOR,Edge.WALL,Edge.WALL,Edge.WALL};
        Edge[] edges4 = new Edge[]{Edge.OPEN,Edge.OPEN,Edge.DOOR,Edge.OPEN};
        Edge[] edges5 = new Edge[]{Edge.DOOR,Edge.OPEN,Edge.OPEN,Edge.WALL};

        grid[0][0] = new Square(MapColor.BLUE, false, 0, 0, map, edges1);
        grid[1][0] = new Square(MapColor.BLUE, false, 0, 1, map, edges2);
        grid[2][0] = new Square(MapColor.RED, true, 0, 2, map, edges3);
        grid[0][1] = new Square(MapColor.BLUE, true, 1, 0, map, edges4);
        grid[1][1] = new Square(MapColor.YELLOW, true, 1, 1, map, edges5);


        map.setGrid(grid);
    }

    /**
     * Check getRoomSquares return exactly all the Map's Squares of a given MapColor
     */
    @Test
    void getRoom() {

        List<Square> blueRoom = new ArrayList<>();
        blueRoom.add(map.getGrid()[0][0]);
        blueRoom.add(map.getGrid()[1][0]);
        blueRoom.add(map.getGrid()[0][1]);

        List<Square> blueRoomMap = map.getRoomSquares(MapColor.BLUE);

        assertTrue(blueRoomMap.containsAll(blueRoom) && blueRoom.containsAll(blueRoomMap));
    }

    /**
     * Checks getVisiblePlayers return exactly the Players in the same room and in the adiacent room through a Door
     */
    @Test
    void getVisibleTargets() {
        Player p1 = new Player(1,PlayerColor.PURPLE);
        Player p2 = new Player(2,PlayerColor.PURPLE);
        Player p3 = new Player(3,PlayerColor.PURPLE);

        map.getGrid()[0][0].addPlayer(p1);
        map.getGrid()[1][1].addPlayer(p2);
        map.getGrid()[2][0].addPlayer(p3);

        List<Player> visible = new ArrayList<>();
        visible.add(p1);
        visible.add(p3);

        List<Player> visibleRes = map.getGrid()[1][0].getVisiblePlayers(1,20);

        assertTrue(visibleRes.containsAll(visible) && visible.containsAll(visibleRes));


    }

    /**
     * Check getSpawoints return exactly the Map's spawnpoints
     */
    @Test
    void getSpawnpoints() {
        List<Square> spawnpoints = new ArrayList<>();
        spawnpoints.add(map.getGrid()[0][1]);
        spawnpoints.add(map.getGrid()[1][1]);
        spawnpoints.add(map.getGrid()[2][0]);

        assertEquals(spawnpoints,map.getSpawnpoints());
    }
}