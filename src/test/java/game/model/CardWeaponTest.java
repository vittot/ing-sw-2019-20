package game.model;

import game.model.effects.*;
import org.junit.jupiter.api.Test;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class CardWeaponTest {
    @Test
    void instanceWeapons ()throws Exception
    {




//        //Distruttore (lock rifle)
//        Effect distr01 = new Effect (2,1,1,1,false,false,0,0,1,2,0,12,false, false,false,0,false,false, Target.PLAYER);
//        Effect distr21 = new Effect (0,1,1,1,false,false,0,0,1,2,0, 12,false,false,false,2,false,false, Target.PLAYER);
        List<Color> priceLRB = new ArrayList<>();
        priceLRB.add(Color.BLUE);
        priceLRB.add(Color.BLUE);
        List<Color> priceLRA = new ArrayList<>();
        priceLRA.add(Color.RED);

        Effect distr01 = new PlainDamageEffect(1,1, 0, Map.MAX_DIST, TargetVisibility.VISIBLE, 2, 1, false, DifferentTarget.ANYONE, false,false);
        Effect distr21 = new PlainDamageEffect(1,1,0, Map.MAX_DIST, TargetVisibility.VISIBLE, 0,1,false,DifferentTarget.NOTTHELAST,false,false);
        CardWeapon cwLR = new CardWeapon("Lock rifle",priceLRB,null,priceLRA,Collections.singletonList(distr01), null,Collections.singletonList(distr21),false,false);

//        //Mitra (machine gun)
//        Effect mitr01 = new Effect (1,0,1,2,false,false,0,0,1,2,0,12,false,false,false,2,false,false, Target.PLAYER);
//        Effect mitr21 = new Effect (1,0,1,1,false,false,0,0, 1,2,0,12,false,true,false,0,false,false, Target.PLAYER);
//        Effect mitr31 = new Effect (1,0,1,1,false,false,0,0,1,2,0,12,false,true,false,1,false,false, Target.PLAYER);
//        Effect mitr32 = new Effect (1,0,1,1,false,false,0,0,1,2,0,12,false,true,false,2,false,false, Target.PLAYER);

        Effect mitr01 = new PlainDamageEffect(1,2,0,Map.MAX_DIST,TargetVisibility.VISIBLE,1,0,false,DifferentTarget.ANYONE,false,false);
        Effect mitr21 = new PlainDamageEffect(1,1,0,Map.MAX_DIST,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.ANYONE,false,false);
        Effect mitr31 = new PlainDamageEffect(0,1,0,Map.MAX_DIST,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.NOTTHELAST,false,false);
        Effect mitr32 = new PlainDamageEffect(0,1,0,Map.MAX_DIST,TargetVisibility.VISIBLE,1,0,false,DifferentTarget.NONEOFTHEPREVIOUS,false,false);

//        //Torpedine (T.H.O.R.)
//        Effect torp01 = new Effect (2,0,1,1,false,false,0,0,1,2,0,12,false,false,false,0,false,false, Target.PLAYER);
//        Effect torp21 = new Effect (1,0,1,1,false,false,0,0,1,2,0,12,true,false,false,2,false,false, Target.PLAYER);
//        Effect torp22 = new Effect (2,0,1,1,false,false,0,0,1,2,0,12,true,false,false,2,false,false, Target.PLAYER);
        Effect torp01 = new PlainDamageEffect(1,1,0,Map.MAX_DIST,TargetVisibility.VISIBLE,2,0,false, DifferentTarget.ANYONE,false,false);
        Effect torp21 = new PlainDamageEffect(1,1,0,Map.MAX_DIST,TargetVisibility.VISIBLE,1,0,false,DifferentTarget.NONEOFTHEPREVIOUS,true,false);
        Effect torp31 = new PlainDamageEffect(1,1,0,Map.MAX_DIST,TargetVisibility.VISIBLE,2,0,false, DifferentTarget.NONEOFTHEPREVIOUS, true,false);
        //(plusOrder flag)
//
//        //Plasma
//        Effect plas01 = new Effect (2,0,1,1,false,false,0,0,1,2,0,12,false,false,false,0,false,false, Target.PLAYER);
//        Effect plas21 = new Effect (0,0,0,0,false,true,1,2,2,2,0,12,false,false,false,0,false,true, Target.SQUARE);
//        // stesso di mitra -- Effect plas22 = new Effect (1,0,1,1,false,false,0,0,1,2,0,12,false,true,false,2,false,false, Target.PLAYER);
        Effect plas01 = new PlainDamageEffect(1,1,0,Map.MAX_DIST,TargetVisibility.VISIBLE,2,0,false,DifferentTarget.ANYONE,false,false);
        Effect plas21 = new MovementEffect(0,0,0,0,1,2,TargetVisibility.VISIBLE,true,TargetVisibility.EVERYWHERE,false,false,false,false,true,DifferentTarget.ANYONE);
        Effect plas31 = new PlainDamageEffect(1,1,0,Map.MAX_DIST,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.ANYONE,false,false);

//        // Precision (whisper)
//        Effect prec01 = new Effect (3,1,1,1,false,false,0,0,1,2,2,12,false,false,false,0,false,false,Target.PLAYER);
        Effect prec01 = new PlainDamageEffect(1,1,2,Map.MAX_DIST,TargetVisibility.VISIBLE,3,1,false,DifferentTarget.ANYONE,false,false);

//
//        //Protonic (Electroshyte)
//        Effect prot01 = new Effect (1,0,1,1,false,false,0,0,1,2,0,0,false,false,false,0,false,false,Target.SQUARE);
//        Effect prot11 = new Effect (2,0,1,1,false,false,0,0,1,2,0,0,false,false,false,0,false,false,Target.SQUARE);
        Effect prot01 = new SquareDamageEffect(0,Game.MAXPLAYERS,0,0,TargetVisibility.VISIBLE,1,0,false,false);
        Effect prot11 = new SquareDamageEffect(0,Game.MAXPLAYERS,0,0,TargetVisibility.VISIBLE,2,0,false,false);
//
//        //Ray (tractor beam)
//        Effect ray01 = new Effect (1,0,1,1,true,false,0,2,2,1,0,12,false,false,false,0,false,false,Target.PLAYER);
//        Effect ray11 = new Effect (3,0,1,1,true,false,-1,-1,2,1,0,2,false,false,false,0,false,false,Target.PLAYER)
        Effect ray01 = new MovementEffect(1,1,0,Map.MAX_DIST,0,2,TargetVisibility.EVERYWHERE,false,TargetVisibility.VISIBLE,false,false,false,false,false,DifferentTarget.ANYONE);
        Effect ray02 = new PlainDamageEffect(1,1,0,Map.MAX_DIST,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.ANYONE,false,false);
        Effect ray11 = new MovementEffect(1,1,0,2,0,2,TargetVisibility.EVERYWHERE,false,TargetVisibility.VISIBLE,true,false,false,false,false,DifferentTarget.ANYONE);
        Effect ray12 = new PlainDamageEffect(1,1,0,0,TargetVisibility.VISIBLE,3,0,true,DifferentTarget.ANYONE,false,false);


//        //Vortex
//        Effect vort01 = new Effect (0,0,1,1,false,false,0,0,1,2,1,12,false,false,false,0,false,false,Target.SQUARE);
//        Effect vort02 = new Effect (2,0,1,1,true,false,0,1,1,2,0,1,false,false,true,0,false,false,Target.PLAYER);
//        Effect vort21 = new Effect (1,0,1,2,true,false,0,1,1,2,0,1,false,false,true,2,false,false,Target.PLAYER);
        Effect vort01 = new SquareDamageEffect(1,1,1,Map.MAX_DIST,TargetVisibility.VISIBLE, 0,0,false,false);
        Effect vort02 = new MovementEffect(1,1,0,1,0,1,TargetVisibility.VISIBLE,false,TargetVisibility.VISIBLE,false,true,false,false,false,DifferentTarget.ANYONE);
        Effect vort03 = new PlainDamageEffect(1,1,0,1,TargetVisibility.VISIBLE,2,0,true,DifferentTarget.ANYONE,false,false);

        Effect vort21 = new MovementEffect(1,1,0,1, 0,1,TargetVisibility.VISIBLE,false,TargetVisibility.VISIBLE,false,true,false,false,false,DifferentTarget.NONEOFTHEPREVIOUS);
        Effect vort22 = new PlainDamageEffect(1,1,0,1,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.NONEOFTHEPREVIOUS,false,false);
        Effect vort23 = new MovementEffect(0,1,0,1, 0,1,TargetVisibility.VISIBLE,false,TargetVisibility.VISIBLE,false,true,false,false,false,DifferentTarget.NONEOFTHEPREVIOUS);
        Effect vort24 = new PlainDamageEffect(0,1,0,1,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.ANYONE,false,false);
        //L'ultimo dev'essere fatto se e solo se è stato fatto il penultimo (ma forse se lastTarget è vuoto si evita il problema)

//        //Vulcanic (furnace)
//        Effect vulc01 = new Effect (1,0,1,1,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.ROOM);
//        Effect vulc11 = new Effect (1,1,1,1,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.SQUARE);
        Effect vulc01 = new RoomDamageEffect(0,Game.MAXPLAYERS,1,Map.MAX_DIST,TargetVisibility.VISIBLE,1,0);
        Effect vulc11 = new SquareDamageEffect(0,Game.MAXPLAYERS,1,1,TargetVisibility.VISIBLE,1,1,false,false);

//
//        //Rocket (heatseeker)
//        Effect rock01 = new Effect (3,0,1,1,false,false,0,0,0,0,1,12,false,false,false,0,false,false,Target.PLAYER);
        Effect rock01 = new PlainDamageEffect(1,1,0,Map.MAX_DIST,TargetVisibility.INVISIBLE,3,0,false,DifferentTarget.ANYONE,false,false);

//
//        //Solar Flash (hellion)
//        Effect flas01 = new Effect (1,0,1,1,false,false,0,0,1,2,1,12,false,false,false,0,false,false, Target.PLAYER);
//        Effect flas02 = new Effect (0,1,1,1,false,false,0,0,1,2,0,0,true,false,false,0,false,false,Target.SQUARE);
//        // stesso di flas01 -- Effect flas11 = new Effect (1,0,1,1,false,false,0,0,1,2,1,12,false,false,false,0,false,false, Target.PLAYER);
//        Effect flas12 = new Effect (0,2,1,1,false,false,0,0,1,2,0,0,true,false,false,0,false,false,Target.SQUARE);
        Effect flash01 = new PlainDamageEffect(1,1,1,Map.MAX_DIST,TargetVisibility.VISIBLE,1,0,false,DifferentTarget.ANYONE,false,false);
        Effect flash02 = new SquareDamageEffect(0,Game.MAXPLAYERS,1,Map.MAX_DIST,TargetVisibility.VISIBLE,0, 1,true,false);
        Effect flash11 = new PlainDamageEffect(1,1,1,Map.MAX_DIST,TargetVisibility.VISIBLE,1,0,false,DifferentTarget.ANYONE,false,false);
        Effect flash12 = new SquareDamageEffect(0,Game.MAXPLAYERS,1,Map.MAX_DIST,TargetVisibility.VISIBLE,0, 2,true,false);

//
//        //Flame
//        Effect flam01 = new Effect (1,0,1,1,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.PLAYER);
//        Effect flam02 = new Effect (1,0,1,1,false,false,0,0,1,2,1,1,true,false,false,0,true,false,Target.PLAYER);
//        Effect flam11 = new Effect (2,0,1,1,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.PLAYER);
//        Effect flam12 = new Effect (1,0,1,1,false,false,0,0,1,2,1,1,true,false,false,0,true,false,Target.PLAYER);
        Effect flam01 = new PlainDamageEffect(1,1,1,1,TargetVisibility.EVERYWHERE,1,0,false,DifferentTarget.ANYONE,false,false);
        Effect flam02 = new PlainDamageEffect(0,1,2,2,TargetVisibility.EVERYWHERE,1,0,false,DifferentTarget.ANYONE,false,true);
        Effect flam11 = new SquareDamageEffect(1,1,1,1,TargetVisibility.EVERYWHERE,2,0,false,false);
        Effect flam12 = new SquareDamageEffect(0,1,2,2,TargetVisibility.EVERYWHERE,1,0,false,true);

//
//        //Lanciagranate (granade launcher)
//        Effect gran01 = new Effect (1,0,1,1,true,false,0,1,1,2,0,12,false,false,false,0,false,false,Target.PLAYER);
//        Effect gran21 = new Effect (1,0,1,1,false,false,0,0,1,2,0,12,false,false,false,0,false,true,Target.SQUARE);
        Effect gran01 =  new PlainDamageEffect(1,1,0,Map.MAX_DIST,TargetVisibility.VISIBLE,1,0,false,DifferentTarget.ANYONE,false,false);
        Effect gran02 = new MovementEffect(0,1,0,Map.MAX_DIST,0,1,TargetVisibility.VISIBLE,false,TargetVisibility.EVERYWHERE,false,false,true,false,false, DifferentTarget.ANYONE);
        Effect gran21 = new SquareDamageEffect(0,Game.MAXPLAYERS,0,Map.MAX_DIST,TargetVisibility.VISIBLE,1,0,false,false);
        //TODO gran21 deve poter essere fatto prima dell'effetto base
//
//        //Lanciarazzi (rocket launcher)
//        Effect lcrz01 = new Effect (2,0,1,1,true,false,0,1,1,2,1,12,false,false,false,0,false,false,Target.PLAYER);
//        Effect lcrz11 = new Effect (3, 0,1,1,true,false,0,1,1,2,1,12,false,false,false,0,false,false,Target.PLAYER);
//        Effect lcrz12 = new Effect (1,0,1,1,false,false,0,0,1,2,0,0,true,false,false,2,false,false,Target.SQUARE);
//        Effect lcrz21 = new Effect (0,0,0,0,false,true,1,2,1,2,0,0,false,false,false,0,false,true,Target.PLAYER);
        Effect lcrz01 = new PlainDamageEffect(1,1,1, Map.MAX_DIST, TargetVisibility.VISIBLE,2,0,false,DifferentTarget.ANYONE,false,false);
        Effect lcrz02 = new MovementEffect(0,1,1, 1,0,1,TargetVisibility.VISIBLE,false,TargetVisibility.EVERYWHERE,false,false,true,false,false,DifferentTarget.ANYONE);
        Effect lcrz21 = new MovementEffect(1,1,0,0,1,2,TargetVisibility.VISIBLE,true,TargetVisibility.EVERYWHERE,false,false,false,false,true,DifferentTarget.ANYONE);
        Effect lcrz11 = new SquareDamageEffect(1,Game.MAXPLAYERS,1,Game.MAXPLAYERS,TargetVisibility.VISIBLE,1,0,false,false);
        Effect lcrz12 = new PlainDamageEffect(1,1,0,0,TargetVisibility.VISIBLE,2,0,false,DifferentTarget.ANYONE,true,false);
        Effect lcrz13 = new MovementEffect(0,1,1, 1,0,1,TargetVisibility.VISIBLE,false,TargetVisibility.EVERYWHERE,false,false,true,false,false,DifferentTarget.ANYONE);
//
//        //Laser (railgun)
//        Effect lsr01 = new Effect (3,0,1,1, false,false,0,0,2,2,0,12,false,false,false,0,false,false,Target.DIRECTION);
//        Effect lsr11 = new Effect (2,0,1,2,false,false,0,0,2,2,0,12,false,false,false,0,true,false,Target.DIRECTION);
        Effect lsr01 = new PlainDamageEffect(1,1,0,Map.MAX_DIST,TargetVisibility.EVERYWHERE,3,0,false,DifferentTarget.ANYONE,false,false);
        Effect lsr11 = new PlainDamageEffect(1,1,0,Map.MAX_DIST,TargetVisibility.EVERYWHERE,3,0,false,DifferentTarget.ANYONE,false,false);
        Effect lsr12 = new PlainDamageEffect(0,1,0,Map.MAX_DIST,TargetVisibility.EVERYWHERE,3,0,false,DifferentTarget.ANYONE,false,true);


//
//        //Photonic blade (cyberblade)
//        Effect phot01 = new Effect (2,0,1,1,false,false,0,0,1,2,0,0,false,false,false,0,false,false,Target.PLAYER);
//        Effect phot21 = new Effect (2,0,1,1,false,false,0,0,1,2,0,0,false,false,false,1,false,false,Target.PLAYER);
//        Effect phot32 = new Effect (0,0,0,0,false,true,0,1,1,2,0,0,false,false,false,0,false,false,Target.PLAYER);
        Effect phot01 = new PlainDamageEffect(1,1,0,0,TargetVisibility.VISIBLE,2,0,false,DifferentTarget.ANYONE,false,false);
        Effect phot21 = new MovementEffect(1,1,0,0,1,1,TargetVisibility.VISIBLE,true,TargetVisibility.VISIBLE,false,false,false,false,true,DifferentTarget.ANYONE);
        Effect phot31 = new PlainDamageEffect(1,1,0,0,TargetVisibility.VISIBLE,2,0,false, DifferentTarget.NONEOFTHEPREVIOUS,false,false);

//
//        //ZX-2
//        Effect zx01 = new Effect (1,2,1,1,false,false,0,0,1,2,0,12,false,false,false,0,false,false,Target.PLAYER);
//        Effect zx11 = new Effect(0,1,0,3,false,false,0,0,1,2,0,12,false,false,false,0,false,false,Target.PLAYER);
        Effect zx01 = new PlainDamageEffect(1,1,0,Map.MAX_DIST,TargetVisibility.VISIBLE,2,1,false,DifferentTarget.ANYONE,false,false);
        Effect zx11 = new PlainDamageEffect(1,3,0,Map.MAX_DIST,TargetVisibility.VISIBLE,0,1,false, DifferentTarget.ANYONE,false,false);
//
//        //Pump (shotgun)
//        Effect pmp01 = new Effect (3,0,1,1,true,false,0,1,1,2,0,0,false,false,false,0,false,false,Target.PLAYER);
//        Effect pmp11 = new Effect (2,0,1,1,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.PLAYER);
        Effect pmp01 = new PlainDamageEffect(1,1,0,0,TargetVisibility.VISIBLE,3,0,false,DifferentTarget.ANYONE,false,false);
        Effect pmp02 = new MovementEffect(0,1,0,1,0,1,TargetVisibility.VISIBLE,false,TargetVisibility.EVERYWHERE,false,false,false,false,true,DifferentTarget.ANYONE);
        Effect pmp11 = new PlainDamageEffect(1,1,1,1,TargetVisibility.VISIBLE,2,0,false,DifferentTarget.ANYONE,false,false);
//
//        //Cyber (powerglove)
//        //effetto cyb14 solo se fatto cyb13 (CORRETTO)
        Effect cyb01 = new PlainDamageEffect(1,1,1,1,TargetVisibility.VISIBLE,1,2,false,DifferentTarget.ANYONE,false,false);
        Effect cyb02 = new MovementEffect(1,1,0,0,1,1,TargetVisibility.VISIBLE,true,TargetVisibility.EVERYWHERE,false,true,false,false,false,DifferentTarget.ANYONE);

        Effect cyb11 = new MovementEffect(1,1,1,1,0,1,TargetVisibility.VISIBLE,true,TargetVisibility.EVERYWHERE,false,false,false,false,false,DifferentTarget.ANYONE);
        Effect cyb12 = new PlainDamageEffect(0,1,0,0,TargetVisibility.VISIBLE,2,0,false,DifferentTarget.ANYONE,false,false);
        Effect cyb13 = new PlainDamageEffect(0,1,1,1,TargetVisibility.VISIBLE,2,0,false,DifferentTarget.ANYONE,false,true);
        Effect cyb14 = new MovementEffect(0,1,1,1,0,1,TargetVisibility.VISIBLE,true,TargetVisibility.EVERYWHERE,false,true,false,false,false,DifferentTarget.ANYONE);





//
//          Onda d'urto (shockwave)
//        Effect ond01 = new Effect (1,0,1,3,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.PLAYER);
        Effect ond01 = new AreaDamageEffect(1,3,1,1,TargetVisibility.VISIBLE,1,0,1);
        Effect ond11 = new AreaDamageEffect(4,4,1,1,TargetVisibility.VISIBLE,1,0,Game.MAXPLAYERS);

        //SLEDGEHAMMER (martello ionico)
        Effect sled01 = new PlainDamageEffect(1,1,0,0,TargetVisibility.VISIBLE,2,0,false,DifferentTarget.ANYONE,false,false);
        Effect sled11 = new PlainDamageEffect(1,1,0,0,TargetVisibility.VISIBLE,3,0,false,DifferentTarget.ANYONE,false,false);
        Effect sled12 = new MovementEffect(1,1,0,1,0,1,TargetVisibility.VISIBLE,false,TargetVisibility.EVERYWHERE,false,false,false,false,false,DifferentTarget.ANYONE);
        Effect sled13 = new MovementEffect(1,1,0,1,0,1,TargetVisibility.VISIBLE,false,TargetVisibility.EVERYWHERE,false,false,false,true,false,DifferentTarget.ANYONE);


        /*** POWER CARD ***/
        //Targeting scope
        Effect ts01 = new PlainDamageEffect(1,1,0,Map.MAX_DIST,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.ANYONE,false,false);

        //Newton
        Effect nw01 = new MovementEffect(1,1,0,1,0,1,TargetVisibility.EVERYWHERE,false,TargetVisibility.EVERYWHERE,false,false,false,false,false,DifferentTarget.ANYONE);
        Effect nw02 = new MovementEffect(0,1,0,1,0,1,TargetVisibility.EVERYWHERE,false,TargetVisibility.EVERYWHERE,false,false,true,true,false,DifferentTarget.ANYONE);

        //Telereporter
        Effect tr01 = new MovementEffect(0,0,0,0,1,Map.MAX_DIST,TargetVisibility.VISIBLE,true,TargetVisibility.EVERYWHERE,false,false,false,false,false,DifferentTarget.ANYONE);

        //Tagback granade
        Effect tg01 = new PlainDamageEffect(1,1,0,Map.MAX_DIST,TargetVisibility.VISIBLE,0,1,false,DifferentTarget.ANYONE,false,false);


        /*JAXBContext contextObj = JAXBContext.newInstance(CardWeapon.class, Effect.class);

        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        FileOutputStream fo = new FileOutputStream("testEffect.xml");

        marshallerObj.marshal(cwLR, fo);
        //marshallerObj.marshal(distr21, fo);*/

    }
}