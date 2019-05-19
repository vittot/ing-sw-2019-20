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
        GameMap map= Game.readMap(2,"mapFile.xml");
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
        map.getGrid()[0][0].addPlayer(p1);
        map.getGrid()[0][0].addPlayer(p2);
        ClientContext.get().setMap(map);
        ClientContext.get().setMyID(1);
    }
    @Test
    void testMap(){
        ClientTextView cli = new ClientTextView();
        Square [][] grid = new Square[4][3];
        GameMap map= Game.readMap(2,"mapFile.xml");
        Square sq = new Square();
        sq.setColor(MapColor.YELLOW);
        Edge [] ed = new Edge[4];
        ed[0] = Edge.WALL;
        ed[1] = Edge.WALL;
        ed[2] = Edge.DOOR;
        ed[3] = Edge.DOOR;
        sq.setEdges(ed);
        for(int i = 0; i < 4 ; i++){
            for(int j = 0 ; j < 3; j++){
                sq.setX(i);
                sq.setY(j);
                grid[i][j] = sq;
            }
        }
        cli.showMap(map.getGrid());
    }
}