package game.model;

import game.model.exceptions.MapOutOfLimitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SquareTest {

    private GameMap map;
    private Game game;

    @BeforeEach
    public void before()  {
        game = new Game(8);
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
        game.setMap(map);
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

    /**
     * Apply a defined damage to every player in a square except the shooter
     */
    @Test
    void addDamageTest(){
        Player p1 = new Player(1, PlayerColor.YELLOW);
        Player p2 = new Player(2, PlayerColor.GREEN);
        Square s = null;
        try {
            s = map.getSquare(0, 0);
        }
        catch(MapOutOfLimitException e){
            e.printStackTrace();
        }
        if(s != null) {
            s.addPlayer(p1);
            s.addPlayer(p2);
        }
        s.addDamage(p2,3);
        assertTrue(p1.getDamage().size() == 3 && p2.getDamage().size() == 0);

    }

    /**
     * Apply a finite number of marks to every player in a square except the shooter
     */
    @Test
    void addMarksTest(){
        Player p1 = new Player(1, PlayerColor.YELLOW);
        Player p2 = new Player(2, PlayerColor.GREEN);
        Square s = null;
        try {
            s = map.getSquare(0, 0);
        }
        catch(MapOutOfLimitException e){
            e.printStackTrace();
        }
        if(s != null) {
            s.addPlayer(p1);
            s.addPlayer(p2);
        }
        s.addThisTurnMarks(p2,3);
        assertTrue(p1.getThisTurnMarks().size() == 3 && p2.getThisTurnMarks().size() == 0);
    }

    /**
     * Return the next square in a specific direction
     */
    @Test
    void getNextSquareTest(){
        Square s1 = map.getGrid()[0][0];
        Square s2 = map.getGrid()[0][1];
        Square s3 = s1.getNextSquare(Direction.RIGHT);
        assertTrue(s3.equals(s2));
    }

    /**
     * Return the square edge in a specific direction
     */
    @Test
    void getEdgesTest(){
        Square s1 = map.getGrid()[0][0];
        Edge e = s1.getEdge(Direction.RIGHT);
        assertTrue(e == Edge.OPEN);
    }

    /**
     * Remove a player from a square
     */
    @Test
    void removePlayerTest(){
        Player p1 = new Player(1, PlayerColor.YELLOW);
        Player p2 = new Player(2, PlayerColor.GREEN);
        Square s = map.getGrid()[0][0];
        s.addPlayer(p1);
        s.addPlayer(p2);
        s.removePlayer(p2);
        assertTrue(!s.getPlayers().contains(p2));
    }

    /**
     * Move all the players positioned in a square in a direction for a defined number of squares
     */
    @Test
    void moveTest(){
        Player p1 = new Player(1, PlayerColor.YELLOW);
        Player p2 = new Player(2, PlayerColor.GREEN);
        p1.setGame(game);
        p2.setGame(game);
        Square s = map.getGrid()[0][0];
        s.addPlayer(p1);
        s.addPlayer(p2);
        p1.setPosition(s);
        p2.setPosition(s);
        try {
            s.move(2, Direction.DOWN);
        } catch (MapOutOfLimitException e) {
            e.printStackTrace();
        }
        assertTrue(map.getGrid()[2][0].getPlayers().contains(p1) && map.getGrid()[2][0].getPlayers().contains(p2));
    }

    /**
     * Return all the invisible squares from another square
     */
    @Test
    void getInvisibleSquaresTest(){
        Player p1 = new Player(1, PlayerColor.YELLOW);
        p1.setGame(game);
        Square s = map.getGrid()[0][0];
        s.addPlayer(p1);
        List<Square> list = s.getInvisibleSquares(0,12);
        List<Square> list2 = new ArrayList<>();
        list2.add(map.getGrid()[1][1]);
        assertTrue(list.equals(list2));
    }

    /**
     * Return all the visible squares from another square
     */
    @Test
    void getVisibleSquaresTest(){
        Player p1 = new Player(1, PlayerColor.YELLOW);
        p1.setGame(game);
        Square s = map.getGrid()[0][1];
        s.addPlayer(p1);
        List<Square> list = s.getVisibleSquares(0,12);
        List<Square> list2 = new ArrayList<>();
        list2.add(map.getGrid()[1][1]);
        list2.add(map.getGrid()[0][1]);
        list2.add(map.getGrid()[0][0]);
        list.removeAll(list2);
        assertTrue(list.isEmpty());
    }

    /**
     * Return all the players in a cardinal direction from a square
     */
    @Test
    void getPlayersInDirections(){
        Player p1 = new Player(1, PlayerColor.YELLOW);
        Player p2 = new Player(2, PlayerColor.GREEN);
        Player p3 = new Player(3, PlayerColor.BLUE);
        p1.setGame(game);
        p2.setGame(game);
        p3.setGame(game);
        Square s = map.getGrid()[0][0];
        s.addPlayer(p1);
        p1.setPosition(s);
        Square s2 = map.getGrid()[1][0];
        s2.addPlayer(p2);
        Square s3 = map.getGrid()[1][1];
        s3.addPlayer(p3);
        p2.setPosition(s2);
        p3.setPosition(s3);
        List<Player> list = s.getPlayersInDirections(1,3);
        List<Player> list2 = new ArrayList();
        list2.add(p2);
        assertTrue(list.equals(list2));
    }

    /**
     * Return true if two squares are equals, false otherwise
     */
    @Test
    void equalsTest(){
        Square s = map.getGrid()[0][0];
        Square s1 = map.getGrid()[1][0];
        assertTrue(!s.equals(s1));
    }

}