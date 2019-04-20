package game.model.effects;

import game.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlainDamageEffectTest {


    private PlainDamageEffect effect;
    private Game game;

    @BeforeEach
    public void before()
    {
        Map map = new Map(1,2,3);
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

        Player p1 = new Player(1,PlayerColor.PURPLE);
        Player p2 = new Player(2,PlayerColor.BLUE);
        Player p3 = new Player(3,PlayerColor.GREEN);
        Player p4 = new Player(4,PlayerColor.YELLOW);
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);

        CardWeapon w = mock(CardWeapon.class);
        p1.setActualWeapon(w);
        p4.setActualWeapon(w);
        when(w.getPreviousTargets()).thenReturn(new ArrayList<>());

        map.getGrid()[0][0].addPlayer(p1);
        map.getGrid()[1][1].addPlayer(p2);
        map.getGrid()[2][0].addPlayer(p3);
        map.getGrid()[1][0].addPlayer(p4);

        game = new Game(players,map,8);



    }

    /**
     * Search for visible targets (used by most weapons)
     */
    @Test
    void searchVisibleTargets() {
        effect = new PlainDamageEffect(1,1, 0, Map.MAX_DIST, TargetVisibility.VISIBLE, 2, 1, false, DifferentTarget.ANYONE, false,false);
        List<List<Target>> targets = effect.searchTarget(game.getPlayers().get(0));
        List<List<Target>> expTargets = new ArrayList<>();
        expTargets.add(new ArrayList<>());
        expTargets.get(0).add( game.getPlayers().get(3) );
        assertEquals(expTargets,targets);
    }

    /**
     * Search for invisible targets (used by Heatseeker)
     */
    @Test
    void searchInvisibleTargets(){
        effect = new PlainDamageEffect(1,1, 0, Map.MAX_DIST, TargetVisibility.INVISIBLE, 2, 1, false, DifferentTarget.ANYONE, false,false);
        List<List<Target>> targets = effect.searchTarget(game.getPlayers().get(3));
        List<List<Target>> expTargets = new ArrayList<>();
        expTargets.add(new ArrayList<>());
        expTargets.get(0).add( game.getPlayers().get(1) );
        assertEquals(expTargets,targets);
    }

    /**
     * Search for targets on a cardinal direction (used by railgun)
     */
    @Test
    void searchDirectionalTargets(){
        effect = new PlainDamageEffect(1,1,1,Map.MAX_DIST,TargetVisibility.DIRECTION,3,0,false,DifferentTarget.ANYONE,false,false);
        List<List<Target>> targets = effect.searchTarget(game.getPlayers().get(3));
        List<List<Target>> expTargets = new ArrayList<>();
        expTargets.add(new ArrayList<>());
        expTargets.get(0).add( game.getPlayers().get(0) );
        expTargets.get(0).add( game.getPlayers().get(1) );
        expTargets.get(0).add( game.getPlayers().get(2) );
        assertEquals(expTargets,targets);
    }

    @Test
    void applyEffect() {
        effect = new PlainDamageEffect(1,1, 0, Map.MAX_DIST, TargetVisibility.VISIBLE, 2, 1, false, DifferentTarget.ANYONE, false,false);
        List<Target> targets = new ArrayList<>();
        Player p1 = game.getPlayers().get(0);
        Player p3 = game.getPlayers().get(2);
        Player p4 = game.getPlayers().get(3);
        targets.add(p3);
        targets.add(p4);
        effect.applyEffect(game.getPlayers().get(0),targets);
        List<PlayerColor> damage = new ArrayList<>();
        damage.add(p1.getColor());
        damage.add(p1.getColor());
        List<PlayerColor> marks = Collections.singletonList(p1.getColor());

        assertTrue(p3.getDamage().equals(damage) && p3.getThisTurnMarks().equals(marks) && p4.getDamage().equals(damage) && p4.getThisTurnMarks().equals(marks));
    }
}