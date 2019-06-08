package game.model.effects;

import game.model.*;
import game.model.exceptions.MapOutOfLimitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AreaDamageEffectTest {

    private AreaDamageEffect effect;
    private Game game;

    @BeforeEach
    public void before()
    {
        GameMap map = new GameMap(1,2,3);
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
        Player p5 = new Player(5,PlayerColor.GREY);
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);
        players.add(p5);

        CardWeapon w = mock(CardWeapon.class);
        p1.setActualWeapon(w);
        p4.setActualWeapon(w);
        p5.setActualWeapon(w);
        when(w.getPreviousTargets()).thenReturn(new ArrayList<>());

        map.getGrid()[0][0].addPlayer(p1);
        map.getGrid()[2][0].addPlayer(p2);
        map.getGrid()[0][0].addPlayer(p3);
        map.getGrid()[1][0].addPlayer(p4);

        game = new Game(players,map,8);
    }

    /**
     * Search for visible targets (used by most weapons)
     */
    @Test
    void searchVisibleTargets() {
        effect = new AreaDamageEffect(1,3,1,1,TargetVisibility.VISIBLE,3,1,2);
        List<Target> targets = effect.searchTarget(game.getPlayer(4));
        List<Target> expTargets = new ArrayList<>();
        expTargets.add( game.getPlayer(1) );
        expTargets.add( game.getPlayer(2) );
        expTargets.add( game.getPlayer(3) );

        assertTrue(expTargets.containsAll(targets) && targets.containsAll(expTargets));
    }

    /**
     * Apply damage to a square and so to a specific range of players in it
     */
    @Test
    void applyDamage() {
        List<Target> targets = new ArrayList<>();
        targets.add(game.getPlayer(1));
        targets.add(game.getPlayer(2));
        effect = new AreaDamageEffect(1,3,1,1,TargetVisibility.VISIBLE,3,1,2);
        effect.applyEffect(game.getPlayer(4), targets);
        assertTrue(game.getPlayers().stream().filter(p -> p.getDamage().size()==3 && p.getThisTurnMarks().size() == 1).count()==2);
    }
}