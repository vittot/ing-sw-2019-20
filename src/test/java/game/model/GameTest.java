package game.model;

import game.controller.PlayerObserver;
import game.controller.XMLParser;
import game.model.effects.*;
import game.model.exceptions.MapOutOfLimitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GameTest {

    private Game g;
    private List<Player> players = new ArrayList<>();

    @BeforeEach
    void before()  {
        int i;
        GameMap m = new GameMap(1,2,3);
        Square[][] grid = new Square[3][2];
        Edge[] edges1 = new Edge[]{Edge.WALL,Edge.OPEN,Edge.OPEN,Edge.WALL};
        Edge[] edges2 = new Edge[]{Edge.OPEN,Edge.WALL,Edge.DOOR,Edge.WALL};
        Edge[] edges3 = new Edge[]{Edge.DOOR,Edge.WALL,Edge.WALL,Edge.WALL};
        Edge[] edges4 = new Edge[]{Edge.OPEN,Edge.OPEN,Edge.DOOR,Edge.OPEN};
        Edge[] edges5 = new Edge[]{Edge.DOOR,Edge.OPEN,Edge.OPEN,Edge.WALL};

        grid[0][0] = new Square(MapColor.BLUE, false, 0, 0, m, edges1);
        grid[1][0] = new Square(MapColor.BLUE, true, 0, 1, m, edges2);
        grid[2][0] = new Square(MapColor.RED, true, 0, 2, m, edges3);
        grid[0][1] = new Square(MapColor.BLUE, true, 1, 0, m, edges4);
        grid[1][1] = new Square(MapColor.YELLOW, true, 1, 1, m, edges5);
        m.setGrid(grid);


        PlayerObserver po = mock(PlayerObserver.class);
        doNothing().when(po).notifyPoints();
        for(i=0;i<3;i++) {
            Player p = new Player(i+1,PlayerColor.values()[i]);
            p.setPlayerObserver(po);
            players.add(p);
        }
        g = new Game(players,m,3);
    }

    /**
     * Check id game
     */
    @Test
    void getId(){
        assertEquals(g.getId(),0);
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
    @Test
    void getCurrentTurn(){
        Player pTurn = g.getCurrentTurn().getCurrentPlayer();
        assertEquals(pTurn,g.getCurrentTurn().getCurrentPlayer());
    }
    /**
    *Check the number of player to be respawned
    */
    @Test
    void getPLayerRespawned(){
        assertEquals(g.getnPlayerToBeRespawned(),0);
    }
    /**
     * Check get player
     */
    @Test
    void getPlayer(){
        assertEquals(players.get(0),g.getPlayer(1));
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
     * Test the victory condition (Game's killboard becomes full)
     */
    @Test
    void checkVictory() {
        Player p1 = g.getPlayers().get(0);
        Player p2 = g.getPlayers().get(1);

        CardPower cp = mock(CardPower.class);
        when(cp.getMapColor()).thenReturn(MapColor.BLUE);
        List<Player> toRespawn;
        //Player p1 kills Player p2
        p2.addDamage(p1,11);
        toRespawn = g.changeTurn();
        for(Player p: toRespawn)
            p.respawn(cp);
        //Player p2 kills Player p1
        p1.addDamage(p2,11);
        toRespawn = g.changeTurn();
        for(Player p: toRespawn)
            p.respawn(cp);
        //Player p1 kills Player p2 with rage
        p2.addDamage(p1,12);
        toRespawn = g.changeTurn();
        for(Player p: toRespawn)
            p.respawn(cp);
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

    /**
     * check map
     * @throws MapOutOfLimitException
     */
    @Test
    void checkMap() throws MapOutOfLimitException{
        g.setMap(XMLParser.readMap(99, "mapFile.xml"));
        GameMap map = new GameMap(99,4,3);
        Edge [] ed = new Edge[4];
        ed[0] = Edge.WALL;
        ed[1] = Edge.WALL;
        ed[2] = Edge.WALL;
        ed[3] = Edge.WALL;
        Square sq0 = new Square(MapColor.YELLOW,false,0,0,g.getMap(),ed);
        assertTrue(g.getMap().getSquare(0,0).getColor().equals(MapColor.YELLOW));

    }

    /**
     * Check read deck
     */
    @Test
    void readDeck() {
        XMLParser.readDeck("effectFile.xml",this.g);
    }

    /**
     * Check ammo deck
     */
    @Test
    void readAmmoDeck()
    {
        List<Color> ammo = new ArrayList<>();
        ammo.add(Color.RED);
        ammo.add(Color.BLUE);
        CardAmmo c = new CardAmmo(ammo,1);
        XMLParser.readAmmoDeck("ammoFile.xml",this.g);
        assertTrue(g.getDeckAmmo().size() == 36 && g.getDeckAmmo().get(35).equals(c));
    }

    /**
     * read power up deck
     */
    @Test
    void readPowerUpDeck()
    {
        List<Color> price = new ArrayList<>();
        SimpleEffect ef1 = new MovementEffect(1,1,0,12,0,12,TargetVisibility.EVERYWHERE,true,TargetVisibility.EVERYWHERE,false,false,false,false,DifferentTarget.ANYONE);
        FullEffect eff = new FullEffect(Collections.singletonList(ef1),"Teleporter","Play this card in any moment of your turn, move your figure and set it down on any square.",price,false);

        CardPower cp = new CardPower(24,"name","desc",Color.YELLOW,false,false,eff);
        XMLParser.readPowerUpDeck("powerupFile.xml",this.g);

        assertTrue(g.getDeckPower().size() == 24 && g.getDeckPower().get(23).equals(cp));

    }

    /**
     * Ranking generation test
     */
    @Test
    void getRankingTest()
    {
        g.getPlayer(2).setPoints(3);
        Ranking ranking = g.getRanking();
        int p = Integer.MAX_VALUE, id=-1;
        boolean correct = true;
        for(Integer i : ranking.getOrderedPoints())
        {
            if(i > p)
                correct = false;
            /*if(e.getValue() == p && e.getKey().getId() < id)
                correct = false;*/
            p = i;
            //id = e.getKey().getId();

        }
        assertTrue(correct);

    }

    /**
     * check the point
     */
    @Test
    void updatePoints() {
        Player p1  = g.getPlayer(1);
        Player p2 = g.getPlayer(2);
        Player p3 = g.getPlayer(3);
        p1.addDamage(p2,3);
        p2.addDamage(p1,4);
        p1.addDamage(p3,6);
        p2.addDamage(p1,2);
        p1.addDamage(p2,7);
        g.changeTurn();
        int points1 = p1.getPoints();
        int points2 = p2.getPoints();
        int points3 = p3.getPoints();

        assertTrue(points2 == 8+1 && points3 == 6 && points1 == 0);
    }

    /**
     * Test the killboard points count
     */
    @Test
    void countKillBoardPoints(){
        Player p1  = g.getPlayer(1);
        Player p2 = g.getPlayer(2);
        Player p3 = g.getPlayer(3);
        g.getKillBoard().add(new Kill(p1,p2,false));
        g.getKillBoard().add(new Kill(p2,p1,true));
        g.getKillBoard().add(new Kill(p2,p3,false));
        g.getKillBoard().add(new Kill(p1,p3,true));
        g.countKillBoardPoints();
        assertTrue(p1.getKillboardpoints() == 8 && p2.getKillboardpoints() == 6 && p3.getKillboardpoints() == 0);

    }

    /**
     * Check if the game is in final frenzy
     */
    @Test
    void isFinal(){
        assertFalse(g.isFinalFreazy());
    }

    /**
     * Check if the last kill of the turn
     */
    @Test
    void getLastKill(){
        Player p1  = g.getPlayer(1);
        Player p2 = g.getPlayer(2);
        g.addThisTurnKill(p1,p2,false);
        assertEquals(g.getLastKill(p2).getKiller(),p1);
    }

    /**
     * Check if this turn kill is filled correctly
     */
    @Test
    void getThisTurnKill(){
        Player p1  = g.getPlayer(1);
        Player p2 = g.getPlayer(2);
        g.addThisTurnKill(p1,p2,false);
        assertEquals(g.getThisTurnKill().size(),1);
    }
}