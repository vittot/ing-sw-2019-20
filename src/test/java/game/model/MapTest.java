package game.model;


import game.model.exceptions.MapOutOfLimitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


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
        List<Square> expSpawnpoints = map.getSpawnpoints();
        assertEquals(spawnpoints,expSpawnpoints);
    }

    /**
     * Return the square identified by the specified x and y coordinates
     */
    @Test
    void getSquare() {
        boolean i = true;
        Square result = null;
        try {
            result = map.getSquare(0, 3);
        }
        catch(MapOutOfLimitException e){
            i = false;
        }
        assertEquals(i, false);
    }

    /**
     * Return the respawn square for a specific room color in the map
     */
    @Test
    void respawnColor() throws MapOutOfLimitException {
        Square result = null;
        result = map.respawnColor(MapColor.BLUE);
        assertEquals(result,map.getSquare(1,0));
    }

    /**
     * Check that sensible player data are correctly serialized when serializing the Map
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws MapOutOfLimitException
     */
    @Test
    void serializationTest() throws IOException, ClassNotFoundException, MapOutOfLimitException {
        Player p1 = new Player(1,PlayerColor.PURPLE);
        Player p2 = new Player(2,PlayerColor.GREEN);
        Player p3 = new Player(3,PlayerColor.GREY);

        CardWeapon cw1 = mock(CardWeapon.class,withSettings().serializable());
        CardWeapon cw2 = mock(CardWeapon.class,withSettings().serializable());
        when(cw1.getId()).thenReturn(1);
        when(cw2.getId()).thenReturn(2);

        CardPower cp1 = mock(CardPower.class,withSettings().serializable());
        CardPower cp2 = mock(CardPower.class,withSettings().serializable());
        when(cp1.getId()).thenReturn(1);
        when(cp2.getId()).thenReturn(2);

        p1.addWeapon(cw1);
        p2.addWeapon(cw2);
        p1.addCardPower(cp1);
        p2.addCardPower(cp2);

        map.getGrid()[0][0].addPlayer(p1);
        map.getGrid()[1][1].addPlayer(p2);
        map.getGrid()[2][0].addPlayer(p3);
        p2.setSerializeEverything(true);

        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(map);
        ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
        ObjectInputStream oi = new ObjectInputStream(bi);
        Map des = (Map)oi.readObject();
        Player des1 = des.getSquare(0,0).getPlayers().get(0);
        Player des2 = des.getSquare(1,1).getPlayers().get(0);
        Player des3 = des.getSquare(0,2).getPlayers().get(0);

        assertTrue(des1.getCardPower() == null && des1.getWeapons() == null && equalWeapons(des2.getWeapons(), p2.getWeapons()) && equalPowerUps(des2.getCardPower(),p2.getCardPower()) && des3.getGame() == null);

    }

    /**
     * Check if two list of weapons are composed by the same weapons, comparing the weapons id
     * @param w1
     * @param w2
     * @return
     */
    boolean equalWeapons(List<CardWeapon> w1, List<CardWeapon> w2)
    {
        if(w1 == null && w2 == null)
            return true;
        if((w1 == null && w2 != null) || (w2 == null && w1 != null))
            return false;
        if(w1.size() != w2.size())
            return false;
        for(int i=0;i<w1.size();i++)
            if(w1.get(i).getId() != w2.get(i).getId())
                return false;

        return true;
    }

    /**
     * Check if two list of powerups are composed by the same powerups, comparing the powerups id
     * @param cp1
     * @param cp2
     * @return
     */
    boolean equalPowerUps(List<CardPower> cp1, List<CardPower> cp2)
    {
        if(cp1 == null && cp2 == null)
            return true;
        if((cp1 == null && cp2 != null) || (cp2 == null && cp1 != null))
            return false;
        if(cp1.size() != cp2.size())
            return false;
        for(int i=0;i<cp1.size();i++)
            if(cp1.get(i).getId() != cp2.get(i).getId())
                return false;

        return true;
    }
}