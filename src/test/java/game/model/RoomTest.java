package game.model;

import game.model.exceptions.InsufficientAmmoException;
import game.model.exceptions.MapOutOfLimitException;
import game.model.exceptions.NoCardWeaponSpaceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RoomTest {

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
     * Apply a defined damage to every player in a room except the shooter
     */
    @Test
    void addDamageTest(){
        Player p1 = new Player(1, PlayerColor.YELLOW);
        Player p2 = new Player(2, PlayerColor.GREEN);
        Player p3 = new Player(3, PlayerColor.GREEN);
        Square s = null, s1 = null, s2 = null;
        try {
            s = map.getSquare(0, 0);
            s1 = map.getSquare(1,0);
            s2 = map.getSquare(1,1);
        }
        catch(MapOutOfLimitException e){
            e.printStackTrace();
        }
        if(s != null && s1 != null && s2 != null) {
            s.addPlayer(p1);
            s1.addPlayer(p2);
            s2.addPlayer(p3);
        }
        Room r = map.getRoomByColor(s.getColor());
        r.addDamage(p3,3);
        assertTrue(p1.getDamage().size() == 3 && p2.getDamage().size() == 3 && p3.getDamage().size() == 0);
    }

    /**
     * Apply a finite number of marks to every player in a room except the shooter
     */
    @Test
    void addMarksTest(){
        Player p1 = new Player(1, PlayerColor.YELLOW);
        Player p2 = new Player(2, PlayerColor.GREEN);
        Player p3 = new Player(3, PlayerColor.GREEN);
        Square s = null, s1 = null, s2 = null;
        try {
            s = map.getSquare(0, 0);
            s1 = map.getSquare(1,0);
            s2 = map.getSquare(1,1);
        }
        catch(MapOutOfLimitException e){
            e.printStackTrace();
        }
        if(s != null && s1 != null && s2 != null) {
            s.addPlayer(p1);
            s1.addPlayer(p2);
            s2.addPlayer(p3);
        }
        Room r = map.getRoomByColor(s.getColor());
        r.addThisTurnMarks(p3,3);
        assertTrue(p1.getThisTurnMarks().size() == 3 && p2.getThisTurnMarks().size() == 3 && p3.getThisTurnMarks().size() == 0);
    }

    /**
     * Return the squares in a room
     */
    @Test
    void getSquaresTest(){
        Square s = null, s1 = null, s2 = null;
        try {
            s = map.getSquare(0, 0);
            s1 = map.getSquare(1,0);
            s2 = map.getSquare(1,1);
        }
        catch(MapOutOfLimitException e){
            e.printStackTrace();
        }

        Room r = map.getRoomByColor(s.getColor());
        List<Square> list = r.getSquares();
        int count = 0;
        for(Square t : list)
            if(t.equals(s) || t.equals(s1))
                count ++;
        assertTrue(count == 2);
    }

    /**
     * Move all the players in a room
     */
    @Test
    void moveTest(){
        Player p1 = new Player(1, PlayerColor.YELLOW);
        Player p2 = new Player(2, PlayerColor.GREEN);
        Player p3 = new Player(3, PlayerColor.GREEN);
        p1.setGame(game);
        p2.setGame(game);
        p3.setGame(game);
        Square s = null, s1 = null, s2 = null;
        try {
            s = map.getSquare(0, 0);
            s1 = map.getSquare(1,0);
            s2 = map.getSquare(1,1);
        }
        catch(MapOutOfLimitException e){
            e.printStackTrace();
        }
        if(s != null && s1 != null && s2 != null) {
            s.addPlayer(p1);
            s1.addPlayer(p2);
            s2.addPlayer(p3);
        }
        Room r = map.getRoomByColor(s.getColor());
        try {
            r.move(1,Direction.DOWN);
        } catch (MapOutOfLimitException e) {
            e.printStackTrace();
        }
        assertTrue(p1.getPosition().equals(map.getGrid()[1][0]) && p2.getPosition().equals(map.getGrid()[1][1]) && p3.getPosition().equals(map.getGrid()[1][1]));
    }

    /**
     * Return true if two rooms are equals, false otherwise
     */
    @Test
    void equalsTest(){
        Square s = null, s1 = null, s2 = null;
        try {
            s = map.getSquare(0, 0);
            s1 = map.getSquare(1,0);
            s2 = map.getSquare(1,1);
        }
        catch(MapOutOfLimitException e){
            e.printStackTrace();
        }
        Room r = map.getRoomByColor(s.getColor());
        Room r1 = map.getRoomByColor(s2.getColor());
        assertTrue(!r.equals(r1));
    }
}
