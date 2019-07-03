package game.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KillTest {

    private Kill k;

    @BeforeEach
    void before(){
        Player killer = mock(Player.class);
        when(killer.getNickName()).thenReturn("killer");
        Player victim = mock(Player.class);
        when(victim.getNickName()).thenReturn("victim");
        k = new Kill (killer, victim, true);
    }

    @Test
    void getKiller() {
        assertEquals("killer",k.getKiller().getNickName());
    }

    @Test
    void setKiller() {
        Player killer2 = mock(Player.class);
        when(killer2.getNickName()).thenReturn("killer2");
        k.setKiller(killer2);
        assertEquals("killer2",k.getKiller().getNickName());
    }

    @Test
    void getVictim() {
        assertEquals("victim",k.getVictim().getNickName());
    }

    @Test
    void setVictim() {
        Player victim2 = mock(Player.class);
        when(victim2.getNickName()).thenReturn("victim2");
        k.setVictim(victim2);
        assertEquals("victim2",k.getVictim().getNickName());
    }

    @Test
    void isRage() {
        assertTrue(k.isRage());
    }

    @Test
    void setRage() {
        k.setRage(false);
        assertFalse(k.isRage());
    }
}