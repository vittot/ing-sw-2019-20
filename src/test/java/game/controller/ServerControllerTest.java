package game.controller;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.clientcommands.CheckValidWeaponRequest;
import game.controller.commands.clientcommands.CounterAttackResponse;
import game.controller.commands.clientcommands.LogoutRequest;
import game.controller.commands.servercommands.OperationCompletedResponse;
import game.model.*;
import game.model.effects.MovementEffect;
import game.model.effects.PlainDamageEffect;
import game.model.effects.SimpleEffect;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ServerControllerTest {

    private ServerController controller;
    private Game g;

    @BeforeEach
    void before(){
        this.controller = new ServerController();
        List<Player> players = new ArrayList<>();
        GameMap map = mock(GameMap.class);
        when(map.getAllPlayers()).thenReturn(players);
        for(int i=0;i<4;i++)
        {
            players.add(new Player(i+1, PlayerColor.values()[i]));
            when(map.getPlayerById(i+1)).thenReturn(players.get(i));
        }
        g = new Game(1,1,8,players);
        g.setMap(map);
        this.controller.startGame(g,players.get(0));
    }

    /**
     * Test the start of the game
     */
    @Test
    void startGameTest()
    {
        this.controller.startGame(g,g.getPlayers().get(0));
        assertTrue(this.controller.getState() == ServerState.WAITING_SPAWN && this.controller.getCurrPlayer().equals(g.getPlayers().get(0)));
    }

    /**
     * Test for checkValidWeapon
     */
    @Test
    void checkValidWeaponRequestHandleTest()
    {
        Player p = this.g.getPlayers().get(0);
        CardWeapon cw = mock(CardWeapon.class);
        p.addWeapon(cw);
        CheckValidWeaponRequest msg  = new CheckValidWeaponRequest(p);
        OperationCompletedResponse answ = (OperationCompletedResponse)controller.handle(msg);
        assertEquals("",answ.getMessage());

    }

    /**
     * Test the counter attack management
     */
    @Test
    void counterAttackResponse()
    {
        CardPower cp = mock(CardPower.class);
        this.g.getPlayer(1).addCardPower(cp);
        CounterAttackResponse msg = new CounterAttackResponse(cp,this.g.getPlayer(2));
        this.controller.handle(msg);
        assertTrue(this.g.getPlayer(1).getCardPower().size()==1 && this.g.getPlayer(2).getThisTurnMarks().size()==1);
    }

    /**
     * Test for user logout
     */
    @Test
    void logoutRequestHandle()
    {
        GameManager.get().addLoggedUser("user");
        LogoutRequest msg = new LogoutRequest("user");
        controller.handle(msg);
        assertFalse(GameManager.get().getUsersLogged().contains("user"));
    }

    @Test
    void endTurnManagement()
    {

    }

}