package game.controller;

import game.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientTextViewTest {
    @BeforeEach
    public void before()
    {
        GameMap mape= Game.readMap(1,"mapFile.xml");
        Player p1= new Player(1,PlayerColor.GREY);
        Player p2= new Player(2,PlayerColor.PURPLE);
        List<Color> ammos = new ArrayList<>();
        ammos.add(Color.BLUE);
        ammos.add(Color.YELLOW);
        ammos.add(Color.RED);
        List<PlayerColor> damages = new ArrayList<>();
        damages.add(PlayerColor.BLUE);
        p1.setAmmo(ammos);
        p1.addDamage(p2,3);
        mape.getGrid()[0][0].addPlayer(p1);
        mape.getGrid()[0][0].addPlayer(p2);
        ClientContext.get().setMap(mape);
        ClientContext.get().setMyID(1);
    }
    @Test
    void testMap(){
        ClientTextView cli = new ClientTextView();
        Player p1= new Player(1,PlayerColor.GREY);
        Player p2= new Player(2,PlayerColor.PURPLE);
        List<Color> ammos = new ArrayList<>();
        CardAmmo ca = new CardAmmo(ammos,1);
        //ammos.add(Color.BLUE);
        ammos.add(Color.YELLOW);
        ammos.add(Color.RED);
        GameMap map= Game.readMap(2,"mapFile.xml");
        map.getGrid()[0][2].setCardAmmo(ca);
        map.getGrid()[0][2].addPlayer(p1);
        map.getGrid()[0][2].addPlayer(p2);
        //cli.showMap(map);
    }
}