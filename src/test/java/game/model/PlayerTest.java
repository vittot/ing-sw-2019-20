package game.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player p;

    @BeforeEach
    void before()
    {
        p = new Player(1,PlayerColor.PURPLE);
        List<Color> ammo = new ArrayList<>();
        ammo.add(Color.RED);
        ammo.add(Color.BLUE);
        ammo.add(Color.BLUE);
        ammo.add(Color.YELLOW);
        ammo.add(Color.YELLOW);
        p.setAmmo(ammo);

        List<CardPower> powerups = new ArrayList<>();
        powerups.add(new CardPower("PW","desc",Color.BLUE,null,false,null));
        powerups.add(new CardPower("PW2","d2",Color.YELLOW,null,false,null));
        p.setCardPower(powerups);
    }


    /**
     * Remove ammo from the Player
     */
    @Test
    void removeAmmo() {
        List<Color> toBeRemoved = new ArrayList<>();
        toBeRemoved.add(Color.RED);
        toBeRemoved.add(Color.BLUE);
        toBeRemoved.add(Color.BLUE);
        List<Color> remainingAmmo = new ArrayList<>();
        remainingAmmo.add(Color.YELLOW);
        remainingAmmo.add(Color.YELLOW);
        p.removeAmmo(toBeRemoved);
        assertTrue(p.getAmmo().containsAll(remainingAmmo) && remainingAmmo.containsAll(p.getAmmo()));

    }

    /**
     * Remove ammo from the Player
     */
    @Test
    void removePowerUp() {
        List<CardPower> toBeRemoved = new ArrayList<>();
        toBeRemoved.add(p.getCardPower().get(1));
        List<CardPower> remainingPW = new ArrayList<>();
        remainingPW.add(p.getCardPower().get(0));
        p.removePowerUp(toBeRemoved);
        assertTrue(p.getCardPower().containsAll(remainingPW) && remainingPW.containsAll(p.getCardPower()));

    }
}