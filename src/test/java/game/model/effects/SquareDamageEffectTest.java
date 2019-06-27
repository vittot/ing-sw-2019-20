package game.model.effects;

import game.model.*;
import game.model.exceptions.MapOutOfLimitException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SquareDamageEffectTest {
    private SquareDamageEffect effect;
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
        List<Player> prevTarget = new ArrayList<>();
        prevTarget.add(p2);
        when(w.getPreviousTargets()).thenReturn(prevTarget);
        when(w.getLastTarget()).thenReturn(p2);

        map.getGrid()[0][0].addPlayer(p1);
        map.getGrid()[1][1].addPlayer(p2);
        map.getGrid()[0][0].addPlayer(p3);
        map.getGrid()[1][0].addPlayer(p4);

        game = new Game(players,map,8);



    }

    /**
     * Search for visible targets (used by most weapons)
     */
    @Test
    void searchVisibleTargets() throws MapOutOfLimitException{
        effect = new SquareDamageEffect(1,1,1,2,TargetVisibility.VISIBLE,1,1,false,false);
        List<Target> targets = effect.searchTarget(game.getPlayer(4));
        List<Target> expTargets = new ArrayList<>();
        expTargets.add( game.getMap().getSquare(0,0) );
        //there are excluded because they do not contain any player:
        /*expTargets.add( game.getMap().getSquare(0,2) );
        expTargets.add( game.getMap().getSquare(1,0) );*/

        assertTrue(expTargets.containsAll(targets) && targets.containsAll(expTargets));
    }

    /**
     * Search from the last target (used by Hellion)
     */
    @Test
    void searchFromTheLastTarget() throws MapOutOfLimitException {
        when(game.getPlayer(1).getActualWeapon().getLastTargetSquare()).thenReturn(game.getPlayer(2).getPosition());
        effect = new SquareDamageEffect(1,1,0,1,TargetVisibility.VISIBLE,1,1,true,false);

        game.getPlayer(4).move(game.getMap().getSquare(1,0));
        game.getPlayer(3).move(game.getMap().getSquare(1,1));

        List<Target> targets = effect.searchTarget(game.getPlayer(1));
        List<Target> expTargets = new ArrayList<>();

        expTargets.add( game.getMap().getSquare(1,0) );
        expTargets.add( game.getMap().getSquare(1,1) );

        assertTrue(expTargets.containsAll(targets) && targets.containsAll(expTargets));
    }

    /**
     * Search mantaining the last direction (used by Flamethrower)
     */
    @Test
    void searchWithLastDirection() throws MapOutOfLimitException {
        when(game.getPlayer(5).getActualWeapon().getLastTargetSquare()).thenReturn(game.getPlayer(4).getPosition());
        when(game.getPlayer(5).getActualWeapon().getLastDirection()).thenReturn(Direction.UP);
        effect = new SquareDamageEffect(1,1,1,1,TargetVisibility.VISIBLE,1,1,true,true);
        List<Target> targets = effect.searchTarget(game.getPlayer(5));
        List<Target> expTargets = new ArrayList<>();
        expTargets.add( game.getMap().getSquare(0,0) );

        assertTrue(expTargets.containsAll(targets) && targets.containsAll(expTargets));
    }

    /**
     * Apply damage to a square and so to a specific range of players in it
     */
    @Test
    void applyDamage() throws MapOutOfLimitException {
        /*effect = new SquareDamageEffect(1,1,1,1,TargetVisibility.VISIBLE,3,1,true,true);
        effect.applyEffect(game.getPlayer(4), Collections.singletonList(game.getMap().getSquare(0,0)));
        assertTrue(game.getMap().getSquare(0,0).getPlayers().stream().filter(p -> p.getDamage().size()==3 && p.getThisTurnMarks().size() == 1).count()==2);
    */}

}