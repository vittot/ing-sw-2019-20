package game.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class GameTest {

    private Game g;

    @BeforeEach
    void before()  {
        int i;
        Map m = new Map(1,2,3);
        Square[][] grid = new Square[3][2];
        Edge[] edges1 = new Edge[]{Edge.WALL,Edge.OPEN,Edge.OPEN,Edge.WALL};
        Edge[] edges2 = new Edge[]{Edge.OPEN,Edge.WALL,Edge.DOOR,Edge.WALL};
        Edge[] edges3 = new Edge[]{Edge.DOOR,Edge.WALL,Edge.WALL,Edge.WALL};
        Edge[] edges4 = new Edge[]{Edge.OPEN,Edge.OPEN,Edge.DOOR,Edge.OPEN};
        Edge[] edges5 = new Edge[]{Edge.DOOR,Edge.OPEN,Edge.OPEN,Edge.WALL};

        grid[0][0] = new Square(MapColor.BLUE, false, 0, 0, m, edges1);
        grid[1][0] = new Square(MapColor.BLUE, false, 0, 1, m, edges2);
        grid[2][0] = new Square(MapColor.RED, true, 0, 2, m, edges3);
        grid[0][1] = new Square(MapColor.BLUE, true, 1, 0, m, edges4);
        grid[1][1] = new Square(MapColor.YELLOW, true, 1, 1, m, edges5);
        m.setGrid(grid);


        List<Player> players = new ArrayList<>();
        for(i=0;i<3;i++) {
            players.add(new Player(i,PlayerColor.values()[i]));
        }
        g = new Game(players,m,3);
    }

    @Test
    void generateDecks() {
        //TODO
    }

    /**
     * Test for normal turn change
     */
    @Test
    void changeTurn() {
        g.changeTurn();
        g.changeTurn();

        Player pTurn = g.getCurrentTurn().getCurrentPlayer();
        Player third  = g.getPlayers().get(2);
        assertEquals(pTurn,third);
    }

    /**
     * Test for turn change after the last player
     */
    @Test
    void changeTurnLast() {
        g.changeTurn();
        g.changeTurn();
        g.changeTurn();

        Player pTurn = g.getCurrentTurn().getCurrentPlayer();
        Player third  = g.getPlayers().get(0);
        assertEquals(pTurn,third);
    }

    /**
     * Test the victory condition
     */
    @Test
    void checkVictory() {
        Player p1 = g.getPlayers().get(0);
        Player p2 = g.getPlayers().get(1);
        p2.addDamage(p1,11);
        p1.addDamage(p2,11);
        p2.addDamage(p1,12);
        assertTrue(g.checkVictory());
    }


    /**
     * Check that refillMap refills all non spawnpoint Squares with an AmmoCard and all spawnpoints with all avaiable CardWeapons
     */
    @Test
    void refillMap() {
        g.getMap().getGrid()[0][0].setCardAmmo(mock(CardAmmo.class));
        g.getMap().getGrid()[2][0].addWeapon(Collections.singletonList(mock(CardWeapon.class)));

        List<CardAmmo> cardAmmoDeck = new ArrayList<>();
        List<CardAmmo> cardAmmoWaste = new ArrayList<>();
        cardAmmoWaste.add(mock(CardAmmo.class));
        cardAmmoWaste.add(mock(CardAmmo.class));
        cardAmmoWaste.add(mock(CardAmmo.class));
        g.setDeckAmmo(cardAmmoDeck);
        g.setAmmoWaste(cardAmmoWaste);
        List<CardWeapon> weaponDeck = new ArrayList<>();
        weaponDeck.add(mock(CardWeapon.class));
        g.setDeckWeapon(weaponDeck);

        g.refillMap();

        boolean hasAmmo=true;
        for(Square s: g.getMap().getNormalSquares())
        {
                if( s.getCardAmmo() == null)
                    hasAmmo = false;
        }
        int weaponOnMap = 0;
        for(Square s : g.getMap().getSpawnpoints())
            if(s.getWeapons() != null)
                weaponOnMap += s.getWeapons().size();

        //All squares which are not respawn must have an ammo card and the weapon card on the map (in respawn squares) have to be 2
        assertTrue(hasAmmo && weaponOnMap == 2);
    }
}