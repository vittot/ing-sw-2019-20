package game.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MapTest {

    @Test
    void getRoom() {
        Map m = new Map(1,3,1);
        Square[][] grid = new Square[3][1];
        grid[0][0] = new Square(Color.BLUE);
        grid[1][0] = new Square(Color.BLUE);
        grid[2][0] = new Square(Color.RED);

        m.setGrid(grid);

        List<Square> blueRoom = new ArrayList<Square>();
        blueRoom.add(grid[0][0]);
        blueRoom.add(grid[1][0]);

        assertEquals(m.getRoom(Color.BLUE), blueRoom);
    }
}