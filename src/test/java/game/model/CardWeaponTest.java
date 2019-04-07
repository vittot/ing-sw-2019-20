package game.model;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CardWeaponTest {
    @Test
    void instanceWeapons ()
    {


        PlainDamageEffect(int minEnemy, int maxEnemy, int minDist, int maxDist, int visibility, int damage, int marks, boolean lastTarget, int differentTarget, boolean chainTarget, boolean sameDirection) {

//        //Distruttore
//        Effect distr01 = new Effect (2,1,1,1,false,false,0,0,1,2,0,12,false, false,false,0,false,false, Target.PLAYER);
//        Effect distr21 = new Effect (0,1,1,1,false,false,0,0,1,2,0, 12,false,false,false,2,false,false, Target.PLAYER);
//
          Effect distr01 = new PlainDamageEffect(1,1, 0, Map.maxDist, TargetVisibility.VISIBLE, 2, 1, false, DifferentTarget.ANYONE, false,false);
          Effect distr21 = new PlainDamageEffect(1,1,0, Map.maxDist, TargetVisibility.VISIBLE, 0,1,false,DifferentTarget.NOTTHELAST,false,false);
//
//        //Mitra
//        Effect mitr01 = new Effect (1,0,1,2,false,false,0,0,1,2,0,12,false,false,false,2,false,false, Target.PLAYER);
//        Effect mitr21 = new Effect (1,0,1,1,false,false,0,0, 1,2,0,12,false,true,false,0,false,false, Target.PLAYER);
//        Effect mitr31 = new Effect (1,0,1,1,false,false,0,0,1,2,0,12,false,true,false,1,false,false, Target.PLAYER);
//        Effect mitr32 = new Effect (1,0,1,1,false,false,0,0,1,2,0,12,false,true,false,2,false,false, Target.PLAYER);

          Effect mitr01 = new PlainDamageEffect(1,2,0,Map.maxDist,TargetVisibility.VISIBLE,1,0,false,DifferentTarget.ANYONE,false,false);
          Effect mitr21 = new PlainDamageEffect(1,1,0,Map.maxDist,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.ANYONE,false,false);
          Effect mitr31 = new PlainDamageEffect(0,1,0,Map.maxDist,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.NOTTHELAST,false,false);
          Effect mitr32 = new PlainDamageEffect(0,1,0,Map.maxDist,TargetVisibility.VISIBLE,1,0,false,DifferentTarget.NONEOFTHEPREVIOUS,false,false);

//        //Torpedine
//        Effect torp01 = new Effect (2,0,1,1,false,false,0,0,1,2,0,12,false,false,false,0,false,false, Target.PLAYER);
//        Effect torp21 = new Effect (1,0,1,1,false,false,0,0,1,2,0,12,true,false,false,2,false,false, Target.PLAYER);
//        Effect torp22 = new Effect (2,0,1,1,false,false,0,0,1,2,0,12,true,false,false,2,false,false, Target.PLAYER);
          Effect torp01 = new PlainDamageEffect(1,1,0,Map.maxDist,TargetVisibility.VISIBLE,2,0,false, DifferentTarget.ANYONE,false,false);
          Effect torp21 = new PlainDamageEffect(1,1,0,Map.maxDist,TargetVisibility.VISIBLE,1,0,false,DifferentTarget.NONEOFTHEPREVIOUS,true,false);
          Effect torp22 = new PlainDamageEffect(1,1,0,Map.maxDist,TargetVisibility.VISIBLE,2,0,false, DifferentTarget.NONEOFTHEPREVIOUS, true,false);

//          public MovementEffect(int minEnemy, int maxEnemy, int minDist, int maxDist, int visibility, boolean moveShooter, int visibilityAfter, boolean myPos, boolean chainMove, boolean lastTarget, boolean sameDirection, boolean beforeBase) {
//        //Plasma
//        Effect plas01 = new Effect (2,0,1,1,false,false,0,0,1,2,0,12,false,false,false,0,false,false, Target.PLAYER);
//        Effect plas21 = new Effect (0,0,0,0,false,true,1,2,2,2,0,12,false,false,false,0,false,true, Target.SQUARE);
//        // stesso di mitra -- Effect plas22 = new Effect (1,0,1,1,false,false,0,0,1,2,0,12,false,true,false,2,false,false, Target.PLAYER);
          Effect plas01 = new PlainDamageEffect(1,1,0,Map.maxDist,TargetVisibility.VISIBLE,2,0,false,DifferentTarget.ANYONE,false,false);
          Effect plas21 = new MovementEffect(0,0,0,0,TargetVisibility.VISIBLE,true,TargetVisibility.EVERYWHERE,false,false,false,false,true);
          Effect plas31 = new PlainDamageEffect(1,1,0,Map.maxDist,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.ANYONE,false,false);

//        // Precision
//        Effect prec01 = new Effect (3,1,1,1,false,false,0,0,1,2,2,12,false,false,false,0,false,false,Target.PLAYER);
          Effect prec01 = new PlainDamageEffect(1,1,2,Map.maxDist,TargetVisibility.VISIBLE,3,1,false,DifferentTarget.ANYONE,false,false);

//
//        //Protonic
//        Effect prot01 = new Effect (1,0,1,1,false,false,0,0,1,2,0,0,false,false,false,0,false,false,Target.SQUARE);
//        Effect prot11 = new Effect (2,0,1,1,false,false,0,0,1,2,0,0,false,false,false,0,false,false,Target.SQUARE);
          Effect prot01 = new SquareDamageEffect(0,Game.MAXPLAYERS,0,0,TargetVisibility.VISIBLE,1,0,false,false);
          Effect prot11 = new SquareDamageEffect(0,Game.MAXPLAYERS,0,0,TargetVisibility.VISIBLE,2,0,false,false);
//
//        //Ray
//        Effect ray01 = new Effect (1,0,1,1,true,false,0,2,2,1,0,12,false,false,false,0,false,false,Target.PLAYER);
//        Effect ray11 = new Effect (3,0,1,1,true,false,-1,-1,2,1,0,2,false,false,false,0,false,false,Target.PLAYER)
          Effect ray01 = new MovementEffect(1,1,0,Map.maxDist,TargetVisibility.EVERYWHERE,false,TargetVisibility.VISIBLE,false,false,false,false,false);
          Effect ray02 = new PlainDamageEffect(1,1,0,Map.maxDist,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.ANYONE,false,false);
          Effect ray11 = new MovementEffect(1,1,0,2,TargetVisibility.EVERYWHERE,false,TargetVisibility.VISIBLE,true,false,false,false,false);
          Effect ray12 = new PlainDamageEffect(1,1,0,0,TargetVisibility.VISIBLE,3,0,true,DifferentTarget.ANYONE,false,false);

    public Effect(int damage, int marks, int minEnemy, int maxEnemy, boolean moveEnemy, boolean moveShooter, int minMove, int maxMove, int visibility, int visibilityAfter, int minDist, int maxDist, boolean chainTarget, boolean prevTarget, boolean chainMove, int differentTarget, boolean sameDirection, boolean beforeBase, Target typeOfTarget) {
//        //Vortex
//        Effect vort01 = new Effect (0,0,1,1,false,false,0,0,1,2,1,12,false,false,false,0,false,false,Target.SQUARE);
//        Effect vort02 = new Effect (2,0,1,1,true,false,0,1,1,2,0,1,false,false,true,0,false,false,Target.PLAYER);
//        Effect vort21 = new Effect (1,0,1,2,true,false,0,1,1,2,0,1,false,false,true,2,false,false,Target.PLAYER);
          Effect vort01 = new SquareDamageEffect(1,1,1,Map.maxDist,TargetVisibility.VISIBLE, 0,0,false,false);
          Effect vort02 = new MovementEffect(1,1,0,1,TargetVisibility.VISIBLE,false,TargetVisibility.VISIBLE,false,true,false,false,false);
          Effect vort03 = new PlainDamageEffect(1,1,0,1,TargetVisibility.VISIBLE,2,0,true,DifferentTarget.ANYONE,false,false);
          Effect vort21 = new MovementEffect(1,1,0,1, TargetVisibility.VISIBLE,false,TargetVisibility.VISIBLE,false,true,false,false,false);
          Effect vort22 = new PlainDamageEffect(1,1,0,1,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.ANYONE,false,false);
          Effect vort23 = new MovementEffect(0,1,0,1, TargetVisibility.VISIBLE,false,TargetVisibility.VISIBLE,false,true,false,false,false);
          Effect vort24 = new PlainDamageEffect(0,1,0,1,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.ANYONE,false,false);
          //L'ultimo dev'essere fatto se e solo se è stato fatto il penultimo

//        //Vulcanic
//        Effect vulc01 = new Effect (1,0,1,1,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.ROOM);
//        Effect vulc11 = new Effect (1,1,1,1,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.SQUARE);
          Effect vulc01 = new RoomDamageEffect(0,Game.MAXPLAYERS,1,Map.maxDist,TargetVisibility.VISIBLE,1,0);
          Effect vulc11 = new SquareDamageEffect(0,Game.MAXPLAYERS,1,1,TargetVisibility.VISIBLE,1,0,false,false);

//
//        //Rocket
//        Effect rock01 = new Effect (3,0,1,1,false,false,0,0,0,0,1,12,false,false,false,0,false,false,Target.PLAYER);
          Effect rock01 = new PlainDamageEffect(1,1,0,Map.maxDist,TargetVisibility.INVISIBLE,3,0,false,DifferentTarget.ANYONE,false,false);

//
//        //Solar Flash
//        Effect flas01 = new Effect (1,0,1,1,false,false,0,0,1,2,1,12,false,false,false,0,false,false, Target.PLAYER);
//        Effect flas02 = new Effect (0,1,1,1,false,false,0,0,1,2,0,0,true,false,false,0,false,false,Target.SQUARE);
//        // stesso di flas01 -- Effect flas11 = new Effect (1,0,1,1,false,false,0,0,1,2,1,12,false,false,false,0,false,false, Target.PLAYER);
//        Effect flas12 = new Effect (0,2,1,1,false,false,0,0,1,2,0,0,true,false,false,0,false,false,Target.SQUARE);
          Effect flash01 = new PlainDamageEffect(1,1,1,Map.maxDist,TargetVisibility.VISIBLE,1,0,false,DifferentTarget.ANYONE,false,false);
          Effect flash02 = new SquareDamageEffect(0,Game.MAXPLAYERS,1,Map.maxDist,TargetVisibility.VISIBLE,0, 1,true,false);
          Effect flash11 = new PlainDamageEffect(1,1,1,Map.maxDist,TargetVisibility.VISIBLE,1,0,false,DifferentTarget.ANYONE,false,false);
          Effect flash12 = new SquareDamageEffect(0,Game.MAXPLAYERS,1,Map.maxDist,TargetVisibility.VISIBLE,0, 2,true,false);

//
//        //Flame
//        Effect flam01 = new Effect (1,0,1,1,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.PLAYER);
//        Effect flam02 = new Effect (1,0,1,1,false,false,0,0,1,2,1,1,true,false,false,0,true,false,Target.PLAYER);
//        Effect flam11 = new Effect (2,0,1,1,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.PLAYER);
//        Effect flam12 = new Effect (1,0,1,1,false,false,0,0,1,2,1,1,true,false,false,0,true,false,Target.PLAYER);


//
//        //Lanciagranate
//        Effect gran01 = new Effect (1,0,1,1,true,false,0,1,1,2,0,12,false,false,false,0,false,false,Target.PLAYER);
//        Effect gran21 = new Effect (1,0,1,1,false,false,0,0,1,2,0,12,false,false,false,0,false,true,Target.SQUARE);
//
//        //Lanciarazzi
//        Effect lcrz01 = new Effect (2,0,1,1,true,false,0,1,1,2,1,12,false,false,false,0,false,false,Target.PLAYER);
//        Effect lcrz11 = new Effect (3, 0,1,1,true,false,0,1,1,2,1,12,false,false,false,0,false,false,Target.PLAYER);
//        Effect lcrz12 = new Effect (1,0,1,1,false,false,0,0,1,2,0,0,true,false,false,2,false,false,Target.SQUARE);
//        Effect lcrz21 = new Effect (0,0,0,0,false,true,1,2,1,2,0,0,false,false,false,0,false,true,Target.PLAYER);
//
//        //Laser
//        Effect lsr01 = new Effect (3,0,1,1, false,false,0,0,2,2,0,12,false,false,false,0,false,false,Target.DIRECTION);
//        Effect lsr11 = new Effect (2,0,1,2,false,false,0,0,2,2,0,12,false,false,false,0,true,false,Target.DIRECTION);
//
//        //Photonic blade
//        Effect phot01 = new Effect (2,0,1,1,false,false,0,0,1,2,0,0,false,false,false,0,false,false,Target.PLAYER);
//        Effect phot21 = new Effect (2,0,1,1,false,false,0,0,1,2,0,0,false,false,false,1,false,false,Target.PLAYER);
//        Effect phot32 = new Effect (0,0,0,0,false,true,0,1,1,2,0,0,false,false,false,0,false,false,Target.PLAYER);
//
//        //ZX-2
//        Effect zx01 = new Effect (1,2,1,1,false,false,0,0,1,2,0,12,false,false,false,0,false,false,Target.PLAYER);
//        Effect zx11 = new Effect(0,1,0,3,false,false,0,0,1,2,0,12,false,false,false,0,false,false,Target.PLAYER);
//
//        //Pump
//        Effect pmp01 = new Effect (3,0,1,1,true,false,0,1,1,2,0,0,false,false,false,0,false,false,Target.PLAYER);
//        Effect pmp11 = new Effect (2,0,1,1,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.PLAYER);
//
//        //Cyber
//        //TODO Effect cyb01 = new Effect ()
//
//        //Onda d'urto
//        Effect ond01 = new Effect (1,0,1,3,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.PLAYER);

        //CardWeapon distruttore = new CardWeapon ()

    }
}