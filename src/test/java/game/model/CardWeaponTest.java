package game.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CardWeaponTest {
    @Test
    void instanceWeapons ()
    {
        //Distruttore
        Effect distr01 = new Effect (2,1,1,1,false,false,0,0,1,2,0,12,false, false,false,0,false,false, Target.PLAYER);
        Effect distr21 = new Effect (0,1,1,1,false,false,0,0,1,2,0, 12,false,false,false,2,false,false, Target.PLAYER);
        //Mitra
        Effect mitr01 = new Effect (1,0,1,2,false,false,0,0,1,2,0,12,false,false,false,2,false,false, Target.PLAYER);
        Effect mitr21 = new Effect (1,0,1,1,false,false,0,0, 1,2,0,12,false,true,false,0,false,false, Target.PLAYER);
        Effect mitr31 = new Effect (1,0,1,1,false,false,0,0,1,2,0,12,false,true,false,1,false,false, Target.PLAYER);
        Effect mitr32 = new Effect (1,0,1,1,false,false,0,0,1,2,0,12,false,true,false,2,false,false, Target.PLAYER);
        //Torpedine
        Effect torp01 = new Effect (2,0,1,1,false,false,0,0,1,2,0,12,false,false,false,0,false,false, Target.PLAYER);
        Effect torp21 = new Effect (1,0,1,1,false,false,0,0,1,2,0,12,true,false,false,2,false,false, Target.PLAYER);
        Effect torp22 = new Effect (2,0,1,1,false,false,0,0,1,2,0,12,true,false,false,2,false,false, Target.PLAYER);

        //Plasma
        Effect plas01 = new Effect (2,0,1,1,false,false,0,0,1,2,0,12,false,false,false,0,false,false, Target.PLAYER);
        Effect plas21 = new Effect (0,0,0,0,false,true,1,2,2,2,0,12,false,false,false,0,false,true, Target.SQUARE);
        // stesso di mitra -- Effect plas22 = new Effect (1,0,1,1,false,false,0,0,1,2,0,12,false,true,false,2,false,false, Target.PLAYER);

        // Precision
        Effect prec01 = new Effect (3,1,1,1,false,false,0,0,1,2,2,12,false,false,false,0,false,false,Target.PLAYER);

        //Protonic
        Effect prot01 = new Effect (1,0,1,1,false,false,0,0,1,2,0,0,false,false,false,0,false,false,Target.SQUARE);
        Effect prot11 = new Effect (2,0,1,1,false,false,0,0,1,2,0,0,false,false,false,0,false,false,Target.SQUARE);

        //Ray
        Effect ray01 = new Effect (1,0,1,1,true,false,0,2,2,1,0,12,false,false,false,0,false,false,Target.PLAYER);
        Effect ray11 = new Effect (3,0,1,1,true,false,-1,-1,2,1,0,2,false,false,false,0,false,false,Target.PLAYER);

        //Vortex
        Effect vort01 = new Effect (0,0,1,1,false,false,0,0,1,2,1,12,false,false,false,0,false,false,Target.SQUARE);
        Effect vort02 = new Effect (2,0,1,1,true,false,0,1,1,2,0,1,false,false,true,0,false,false,Target.PLAYER);
        Effect vort21 = new Effect (1,0,1,2,true,false,0,1,1,2,0,1,false,false,true,2,false,false,Target.PLAYER);

        //Vulcanic
        Effect vulc01 = new Effect (1,0,1,1,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.ROOM);
        Effect vulc11 = new Effect (1,1,1,1,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.SQUARE);

        //Rocket
        Effect rock01 = new Effect (3,0,1,1,false,false,0,0,0,0,1,12,false,false,false,0,false,false,Target.PLAYER);

        //Solar Flash
        Effect flas01 = new Effect (1,0,1,1,false,false,0,0,1,2,1,12,false,false,false,0,false,false, Target.PLAYER);
        Effect flas02 = new Effect (0,1,1,1,false,false,0,0,1,2,0,0,true,false,false,0,false,false,Target.SQUARE);
        // stesso di flas01 -- Effect flas11 = new Effect (1,0,1,1,false,false,0,0,1,2,1,12,false,false,false,0,false,false, Target.PLAYER);
        Effect flas12 = new Effect (0,2,1,1,false,false,0,0,1,2,0,0,true,false,false,0,false,false,Target.SQUARE);

        //Flame
        Effect flam01 = new Effect (1,0,1,1,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.PLAYER);
        Effect flam02 = new Effect (1,0,1,1,false,false,0,0,1,2,1,1,true,false,false,0,true,false,Target.PLAYER);
        Effect flam11 = new Effect (2,0,1,1,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.PLAYER);
        Effect flam12 = new Effect (1,0,1,1,false,false,0,0,1,2,1,1,true,false,false,0,true,false,Target.PLAYER);

        //Lanciagranate
        Effect gran01 = new Effect (1,0,1,1,true,false,0,1,1,2,0,12,false,false,false,0,false,false,Target.PLAYER);
        Effect gran21 = new Effect (1,0,1,1,false,false,0,0,1,2,0,12,false,false,false,0,false,true,Target.SQUARE);

        //Lancuìiarazzi
        Effect lcrz01 = new Effect (2,0,1,1,true,false,0,1,1,2,1,12,false,false,false,0,false,false,Target.PLAYER);
        Effect lcrz21 = new Effect (0,0,0,0,false,true,1,2,1,2,0,0,false,false,false,0,false,true,Target.PLAYER);
        Effect lcrz22 = new Effect (

    }
}