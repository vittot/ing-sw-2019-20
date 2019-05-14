package game.controller;

import game.model.Action;
import game.model.Edge;
import game.model.MapColor;
import game.model.Square;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientTextViewTest {
    @Test
    void testMap(){
        ClientTextView cli = new ClientTextView();
        Square [][] grid = new Square[4][3];
        Square sq = new Square();
        sq.setColor(MapColor.YELLOW);
        Edge [] ed = new Edge[4];
        ed[0] = Edge.DOOR;
        ed[1] = Edge.DOOR;
        ed[2] = Edge.DOOR;
        ed[3] = Edge.WALL;
        sq.setEdges(ed);
        for(int i = 0; i < 4 ; i++){
            for(int j = 0 ; j < 3; j++){
                sq.setX(i);
                sq.setY(j);
                grid[i][j] = sq;
            }
        }
        cli.showMap(grid);
    }
}