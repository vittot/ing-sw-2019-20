package game.model;


import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class TurnTest {

    private Action action;
    private AdrenalineLevel al;
    private Turn turn;
    private List <Action> actionList;

    @BeforeEach
    public void before(){
        turn = new Turn();
        actionList = new ArrayList<Action>();
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
    void newAction(){
        turn.setNumOfActions(2);
        turn.newAction(action,al);
        newAction1(actionList.get(0));
        newAction2(actionList.get(1));
        newAction3(actionList.get(2));

    }

    @Test
    void newAction1(Action actionList) {
        assertEquals(turn.getActionList().get(0),actionList);

    }

    @Test
    void newAction2(Action actionList) {
        assertEquals(turn.getActionList().get(1),actionList);

    }

    @Test
    void newAction3(Action actionList) {
        assertEquals(turn.getActionList().get(2),actionList);
    }
}