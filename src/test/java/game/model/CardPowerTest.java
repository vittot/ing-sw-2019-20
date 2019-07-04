package game.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardPowerTest {
    CardPower cp ;
    @BeforeEach
    void before (){
        cp = new CardPower(1,"name","cardpower",Color.BLUE,false,false,null);
    }
    @Test
    void getId() {
        assertEquals(cp.getId(), 1);
    }

    @Test
    void getName() {
        assertEquals(cp.getName(), "name");
    }

    @Test
    void getColor() {
        assertEquals(cp.getColor(), Color.BLUE);
    }

    @Test
    void isUseWhenDamaged() {
        assertEquals(cp.isUseWhenDamaged(), false);
    }

    @Test
    void isUseWhenAttacking() {
        assertEquals(cp.isUseWhenAttacking(), false);
    }
}