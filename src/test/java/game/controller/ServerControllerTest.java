package game.controller;

import game.model.effects.MovementEffect;
import game.model.effects.PlainDamageEffect;
import game.model.effects.SimpleEffect;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ServerControllerTest {

    private <T extends SimpleEffect> int method(T t)
    {
        return 0;
    }

    private int method(MovementEffect m)
    {
        return 1;
    }

    @Test
    void test()
    {
        SimpleEffect p = mock(PlainDamageEffect.class);
        SimpleEffect m = mock(MovementEffect.class);
        int a = method(p);
        int b = method(m);
        assertTrue(a == 0 && b == 1);
    }


}