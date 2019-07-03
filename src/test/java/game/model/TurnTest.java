package game.model;


import game.model.exceptions.NoResidualActionAvaiableException;
import org.junit.jupiter.api.BeforeAll;
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
    void newAction() throws NoResidualActionAvaiableException {
        turn.setNumOfActions(2);
        turn.newAction(action,al);
        assertEquals(actionList,turn.getActionList());

    }


}