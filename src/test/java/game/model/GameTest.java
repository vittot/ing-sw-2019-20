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
        Map m = mock(Map.class);
        List<Player> players = new ArrayList<>();
        for(i=0;i<3;i++) {
            players.add(mock(Player.class));
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
        g.addKill(p1,p2,false);
        g.addKill(p2,p2,false);
        g.addKill(p1,p2,true);
        assertTrue(g.checkVictory());
    }
}