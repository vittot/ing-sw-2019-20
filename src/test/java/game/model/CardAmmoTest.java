package game.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardAmmoTest {
    CardAmmo ca ;
    @BeforeEach
    void before(){
        List<Color> colors = new ArrayList<>();
        colors.add(Color.BLUE);
        colors.add(Color.BLUE);
        colors.add(Color.YELLOW);

        ca = new CardAmmo(colors, 0);
    }

    @Test
    void getAmmo() {
        List<Color> color = new ArrayList<>();
        color.add(Color.BLUE);
        color.add(Color.BLUE);
        color.add(Color.YELLOW);
        assertEquals(color, ca.getAmmo());
    }

    @Test
    void getCardPower() {
        int i = 0;
        assertEquals(i, ca.getCardPower());
    }

    @Test
    void equals1() {
        List<Color> color = new ArrayList<>();
        color.add(Color.BLUE);
        color.add(Color.BLUE);
        color.add(Color.YELLOW);
        CardAmmo c = new CardAmmo(color, 0 );
        assertTrue(ca.equals(c));
    }
}