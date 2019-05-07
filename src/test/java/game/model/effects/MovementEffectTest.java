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

class MovementEffectTest {
    private MovementEffect effect;
    private Game game;

    @BeforeEach
    public void before()
    {
        Map map = new Map(1,2,3);
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

        Player p1 = new Player(1,PlayerColor.PURPLE);
        Player p2 = new Player(2,PlayerColor.BLUE);
        Player p3 = new Player(3,PlayerColor.GREEN);
        Player p4 = new Player(4,PlayerColor.YELLOW);
        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);
        players.add(p3);
        players.add(p4);

        CardWeapon w = new CardWeapon(Direction.DOWN, map.getGrid()[0][0]);
        p1.setActualWeapon(w);
        //p2.setActualWeapon(w);
        p3.setActualWeapon(w);
        p4.setActualWeapon(w);
        //when(w.getPreviousTargets()).thenReturn(new ArrayList<>());
        //w.setPreviousTargets(new ArrayList<>());

        map.getGrid()[0][0].addPlayer(p1);
        map.getGrid()[1][1].addPlayer(p2);
        map.getGrid()[2][0].addPlayer(p3);
        map.getGrid()[0][1].addPlayer(p4);

        game = new Game(players,map,8);
        Turn turn = new Turn(p3,game);
        game.setCurrentTurn(turn);
        game.getPlayers().get(3).setGame(game);
        game.getPlayers().get(1).setGame(game);
        game.getPlayers().get(2).setGame(game);
    }

    @Test
    void selectMoved() {
        effect = new MovementEffect(1,1,1,3,1,3,TargetVisibility.VISIBLE,false,TargetVisibility.EVERYWHERE,false,false,false,false,DifferentTarget.ANYONE);
        List<Target> targets = effect.searchTarget(game.getPlayers().get(3));
        //I expect p1, p2
        List<Target> expected = new ArrayList<>();
        expected.add(game.getPlayers().get(0));
        expected.add(game.getPlayers().get(1));
        assertTrue(expected.containsAll(targets) && targets.containsAll(expected));
    }

    /**
     * Test for searchTarget that return possible destination for the player movement
     */
    @Test
    void searchTarget() {
        effect = new MovementEffect(1,1,1,3,1,2,TargetVisibility.VISIBLE,true,TargetVisibility.EVERYWHERE,false,false,false,false,DifferentTarget.ANYONE);
        List<Square> targets = effect.selectPosition(game.getPlayers().get(1));
        List<Square> squareCheck = new ArrayList<>();
        //squareCheck.add(game.getMap().getGrid()[1][0]);
        squareCheck.add(game.getMap().getGrid()[0][0]);
        squareCheck.add(game.getMap().getGrid()[0][1]);
        assertTrue(squareCheck.containsAll(targets) && targets.containsAll(squareCheck));
    }

    /**
     * Test for searchTarget (with sameDirection = true) that return possible destination for the player movement
     */
    @Test
    void searchTargetSameDirection() {
        //game.getCurrentTurn().setCurrentPlayer(game.getPlayers().get(0));
        effect = new MovementEffect(1,1,1,2,1,2,TargetVisibility.VISIBLE,true,TargetVisibility.EVERYWHERE,false,false,false,true,DifferentTarget.ANYONE);
        List<Square> targets = effect.selectPosition(game.getPlayers().get(0));
        List<Square> squareCheck = new ArrayList<>();
        squareCheck.add(game.getMap().getGrid()[1][0]);
        squareCheck.add(game.getMap().getGrid()[2][0]);
        assertTrue(squareCheck.containsAll(targets) && targets.containsAll(squareCheck));
    }

    /**
     * Test for searchTarget (with chainMove = true) that return possible destination for the player movement
     */
    @Test
    void searchTargetChainMove() {
        //game.getCurrentTurn().setCurrentPlayer(game.getPlayers().get(0));
        effect = new MovementEffect(1,1,1,2,1,2,TargetVisibility.VISIBLE,true,TargetVisibility.EVERYWHERE,false,true,false,false,DifferentTarget.ANYONE);
        List<Square> targets = effect.selectPosition(game.getPlayers().get(2));
        List<Square> squareCheck = new ArrayList<>();
        squareCheck.add(game.getMap().getGrid()[0][0]);
        assertTrue(squareCheck.containsAll(targets) && targets.containsAll(squareCheck));
    }

    /**
     * Test for searchTarget (with VisibilityAfter = Visible) that return possible destination for the player movement
     */
    @Test
    void searchTargetVisibilityAfter() {
        //game.getCurrentTurn().setCurrentPlayer(game.getPlayers().get(0));
        effect = new MovementEffect(1,1,1,4,1,4,TargetVisibility.VISIBLE,true,TargetVisibility.VISIBLE,false,false,false,false,DifferentTarget.ANYONE);
        List<Square> targets = effect.selectPosition(game.getPlayers().get(1));
        List<Square> squareCheck = new ArrayList<>();
        squareCheck.add(game.getMap().getGrid()[1][0]);
        squareCheck.add(game.getMap().getGrid()[2][0]);
        assertTrue(squareCheck.containsAll(targets) && targets.containsAll(squareCheck));
    }

    /**
     * Check if the damage is correctly applied with the correct marks upgrade
     */
    @Test
    void applyEffect() {
        effect = new MovementEffect(1,1,1,3,1,3,TargetVisibility.VISIBLE,true,TargetVisibility.EVERYWHERE,false,false,false,false,DifferentTarget.ANYONE);
        List<Target> target = new ArrayList<>();
        target.add(game.getMap().getGrid()[0][0]);
        effect.applyEffect(game.getPlayers().get(1), target);
        assertTrue(game.getPlayers().get(1).getPosition().getX()==0 && game.getPlayers().get(1).getPosition().getY()==0);
    }


}