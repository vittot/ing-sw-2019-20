package game.model;


import game.model.exceptions.NoResidualActionAvailableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class TurnTest {

    private Action action;
    private AdrenalineLevel al;
    private Turn turn;
    private List <Action> actionList;

    @BeforeEach
    public void before(){
        Game g = mock(Game.class);
        Player p = mock(Player.class);
        CardWeapon cw = mock(CardWeapon.class);
        when(g.getPlayers()).thenReturn(new ArrayList<>());
        doNothing().when(p).rifleActualWeapon(); //not really necessary, just to be clear
        turn = new Turn(p,g);
        actionList = new ArrayList<>();
        actionList.add(Action.MOVEMENT);
        actionList.add(Action.MOVEMENT);
        actionList.add(Action.GRAB);
        action = Action.GRAB;
        al = AdrenalineLevel.GRABLEVEL;
    }

    /**
     * Check the Movement list return
     */
    @Test
    void newAction() throws NoResidualActionAvailableException {
        turn.setNumOfActions(2);
        turn.newAction(action,al);
        assertEquals(actionList,turn.getActionList());
    }

    /**
     * Apply a single step action if it's possible
     */
    @Test
    void applyStepTest() throws NoResidualActionAvailableException {
        turn.setNumOfActions(2);
        turn.newAction(action,al);
        turn.applyStep(Action.MOVEMENT);
        turn.applyStep(Action.MOVEMENT);
        assertTrue(turn.applyStep(Action.GRAB) && !turn.applyStep(Action.MOVEMENT));
    }

    /**
     * Manage the new turn creation
     */
    @Test
    void manageCurrPlayerTest(){
        Player p1 = new Player(1, PlayerColor.GREEN);
        Player p2 = new Player(2, PlayerColor.PURPLE);
        turn.setCurrentPlayer(p1);
        turn.setNumOfActions(0);
        turn.newTurn(p2,false);
        assertTrue(turn.getCurrentPlayer().equals(p2) && turn.getNumOfActions() == 2);
    }

    /**
     * Return the action list
     */
    @Test
    void createNewActionListTest() throws NoResidualActionAvailableException {
        Player p1 = new Player(1, PlayerColor.GREEN);
        turn.setCurrentPlayer(p1);
        List<Action> act = turn.newAction(Action.MOVEMENT,AdrenalineLevel.NONE);
        if(turn.getNumOfActions()>0)
            act = turn.newAction(Action.GRAB, AdrenalineLevel.GRABLEVEL);
        assertTrue(act.equals(actionList));
    }

    /**
     * Return the action list
     */
    @Test
    void contIfFrenzyTest() throws NoResidualActionAvailableException {
        Player p1 = new Player(1, PlayerColor.GREEN);
        turn.setCurrentPlayer(p1);
        assertFalse(turn.isFinalFrenzy());
    }
}