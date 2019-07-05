package game.model;

import game.model.effects.*;
import game.model.exceptions.InsufficientAmmoException;
import org.junit.jupiter.api.Test;

//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.Marshaller;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CardWeaponTest {

    /**
     * instance all the different kind of game weapon
     * @throws Exception
     */
    @Test
    void instanceWeapons ()throws Exception
    {
//        //Distruttore (lock rifle)
//        SimpleEffect distr01 = new SimpleEffect (2,1,1,1,false,false,0,0,1,2,0,12,false, false,false,0,false,false, Target.PLAYER);
//        SimpleEffect distr21 = new SimpleEffect (0,1,1,1,false,false,0,0,1,2,0, 12,false,false,false,2,false,false, Target.PLAYER);
        List<Color> priceLRB = new ArrayList<>();
        priceLRB.add(Color.BLUE);
        priceLRB.add(Color.BLUE);
        List<Color> priceLRA = new ArrayList<>();
        priceLRA.add(Color.RED);
        List<String> effectDesc = new ArrayList<>();

        SimpleEffect distr01 = new PlainDamageEffect(1,1, 0, GameMap.MAX_DIST, TargetVisibility.VISIBLE, 2, 1, false, DifferentTarget.ANYONE, false,false);
        SimpleEffect distr21 = new PlainDamageEffect(1,1,0, GameMap.MAX_DIST, TargetVisibility.VISIBLE, 0,1,false,DifferentTarget.NOTTHELAST,false,false);
        CardWeapon cwLR = new CardWeapon("Lock rifle",priceLRB,null,null,null,false,false);

//        //Mitra (machine gun)
//        SimpleEffect mitr01 = new SimpleEffect (1,0,1,2,false,false,0,0,1,2,0,12,false,false,false,2,false,false, Target.PLAYER);
//        SimpleEffect mitr21 = new SimpleEffect (1,0,1,1,false,false,0,0, 1,2,0,12,false,true,false,0,false,false, Target.PLAYER);
//        SimpleEffect mitr31 = new SimpleEffect (1,0,1,1,false,false,0,0,1,2,0,12,false,true,false,1,false,false, Target.PLAYER);
//        SimpleEffect mitr32 = new SimpleEffect (1,0,1,1,false,false,0,0,1,2,0,12,false,true,false,2,false,false, Target.PLAYER);

        SimpleEffect mitr01 = new PlainDamageEffect(1,2,0, GameMap.MAX_DIST,TargetVisibility.VISIBLE,1,0,false,DifferentTarget.ANYONE,false,false);
        SimpleEffect mitr21 = new PlainDamageEffect(1,1,0, GameMap.MAX_DIST,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.ANYONE,false,false);
        SimpleEffect mitr31 = new PlainDamageEffect(0,1,0, GameMap.MAX_DIST,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.NOTTHELAST,false,false);
        SimpleEffect mitr32 = new PlainDamageEffect(0,1,0, GameMap.MAX_DIST,TargetVisibility.VISIBLE,1,0,false,DifferentTarget.NONEOFTHEPREVIOUS,false,false);

//        //Torpedine (T.H.O.R.)
//        SimpleEffect torp01 = new SimpleEffect (2,0,1,1,false,false,0,0,1,2,0,12,false,false,false,0,false,false, Target.PLAYER);
//        SimpleEffect torp21 = new SimpleEffect (1,0,1,1,false,false,0,0,1,2,0,12,true,false,false,2,false,false, Target.PLAYER);
//        SimpleEffect torp22 = new SimpleEffect (2,0,1,1,false,false,0,0,1,2,0,12,true,false,false,2,false,false, Target.PLAYER);
        SimpleEffect torp01 = new PlainDamageEffect(1,1,0, GameMap.MAX_DIST,TargetVisibility.VISIBLE,2,0,false, DifferentTarget.ANYONE,false,false);
        SimpleEffect torp21 = new PlainDamageEffect(1,1,0, GameMap.MAX_DIST,TargetVisibility.VISIBLE,1,0,false,DifferentTarget.NONEOFTHEPREVIOUS,true,false);
        SimpleEffect torp31 = new PlainDamageEffect(1,1,0, GameMap.MAX_DIST,TargetVisibility.VISIBLE,2,0,false, DifferentTarget.NONEOFTHEPREVIOUS, true,false);
        //(plusOrder flag)
//
//        //Plasma
//        SimpleEffect plas01 = new SimpleEffect (2,0,1,1,false,false,0,0,1,2,0,12,false,false,false,0,false,false, Target.PLAYER);
//        SimpleEffect plas21 = new SimpleEffect (0,0,0,0,false,true,1,2,2,2,0,12,false,false,false,0,false,true, Target.SQUARE);
//        // stesso di mitra -- SimpleEffect plas22 = new SimpleEffect (1,0,1,1,false,false,0,0,1,2,0,12,false,true,false,2,false,false, Target.PLAYER);
        SimpleEffect plas01 = new PlainDamageEffect(1,1,0, GameMap.MAX_DIST,TargetVisibility.VISIBLE,2,0,false,DifferentTarget.ANYONE,false,false);
        SimpleEffect plas21 = new MovementEffect(0,0,0,0,1,2,TargetVisibility.VISIBLE,true,TargetVisibility.EVERYWHERE,false,false,false,false,DifferentTarget.ANYONE);
        SimpleEffect plas31 = new PlainDamageEffect(1,1,0, GameMap.MAX_DIST,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.ANYONE,false,false);

//        // Precision (whisper)
//        SimpleEffect prec01 = new SimpleEffect (3,1,1,1,false,false,0,0,1,2,2,12,false,false,false,0,false,false,Target.PLAYER);
        SimpleEffect prec01 = new PlainDamageEffect(1,1,2, GameMap.MAX_DIST,TargetVisibility.VISIBLE,3,1,false,DifferentTarget.ANYONE,false,false);

//
//        //Protonic (Electroshyte)
//        SimpleEffect prot01 = new SimpleEffect (1,0,1,1,false,false,0,0,1,2,0,0,false,false,false,0,false,false,Target.SQUARE);
//        SimpleEffect prot11 = new SimpleEffect (2,0,1,1,false,false,0,0,1,2,0,0,false,false,false,0,false,false,Target.SQUARE);
        SimpleEffect prot01 = new SquareDamageEffect(0,Game.MAXPLAYERS,0,0,TargetVisibility.VISIBLE,1,0,false,false);
        SimpleEffect prot11 = new SquareDamageEffect(0,Game.MAXPLAYERS,0,0,TargetVisibility.VISIBLE,2,0,false,false);
//
//        //Ray (tractor beam)
//        SimpleEffect ray01 = new SimpleEffect (1,0,1,1,true,false,0,2,2,1,0,12,false,false,false,0,false,false,Target.PLAYER);
//        SimpleEffect ray11 = new SimpleEffect (3,0,1,1,true,false,-1,-1,2,1,0,2,false,false,false,0,false,false,Target.PLAYER)
        SimpleEffect ray01 = new MovementEffect(1,1,0, GameMap.MAX_DIST,0,2,TargetVisibility.EVERYWHERE,false,TargetVisibility.VISIBLE,false,false,false,false,DifferentTarget.ANYONE);
        SimpleEffect ray02 = new PlainDamageEffect(1,1,0, GameMap.MAX_DIST,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.ANYONE,false,false);
        SimpleEffect ray11 = new MovementEffect(1,1,0,2,0,2,TargetVisibility.EVERYWHERE,false,TargetVisibility.VISIBLE,true,false,false,false,DifferentTarget.ANYONE);
        SimpleEffect ray12 = new PlainDamageEffect(1,1,0,0,TargetVisibility.VISIBLE,3,0,true,DifferentTarget.ANYONE,false,false);


//        //Vortex
//        SimpleEffect vort01 = new SimpleEffect (0,0,1,1,false,false,0,0,1,2,1,12,false,false,false,0,false,false,Target.SQUARE);
//        SimpleEffect vort02 = new SimpleEffect (2,0,1,1,true,false,0,1,1,2,0,1,false,false,true,0,false,false,Target.PLAYER);
//        SimpleEffect vort21 = new SimpleEffect (1,0,1,2,true,false,0,1,1,2,0,1,false,false,true,2,false,false,Target.PLAYER);
        SimpleEffect vort01 = new SquareDamageEffect(1,1,1, GameMap.MAX_DIST,TargetVisibility.VISIBLE, 0,0,false,false);
        SimpleEffect vort02 = new MovementEffect(1,1,0,1,0,1,TargetVisibility.VISIBLE,false,TargetVisibility.VISIBLE,false,true,false,false,DifferentTarget.ANYONE);
        SimpleEffect vort03 = new PlainDamageEffect(1,1,0,1,TargetVisibility.VISIBLE,2,0,true,DifferentTarget.ANYONE,false,false);

        SimpleEffect vort21 = new MovementEffect(1,1,0,1, 0,1,TargetVisibility.VISIBLE,false,TargetVisibility.VISIBLE,false,true,false,false,DifferentTarget.NONEOFTHEPREVIOUS);
        SimpleEffect vort22 = new PlainDamageEffect(1,1,0,1,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.NONEOFTHEPREVIOUS,false,false);
        SimpleEffect vort23 = new MovementEffect(0,1,0,1, 0,1,TargetVisibility.VISIBLE,false,TargetVisibility.VISIBLE,false,true,false,false,DifferentTarget.NONEOFTHEPREVIOUS);
        SimpleEffect vort24 = new PlainDamageEffect(0,1,0,1,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.ANYONE,false,false);
        //L'ultimo dev'essere fatto se e solo se è stato fatto il penultimo (ma forse se lastTarget è vuoto si evita il problema)

//        //Furnace
//        SimpleEffect vulc01 = new SimpleEffect (1,0,1,1,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.ROOM);
//        SimpleEffect vulc11 = new SimpleEffect (1,1,1,1,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.SQUARE);
        SimpleEffect vulc01 = new RoomDamageEffect(0,Game.MAXPLAYERS,1, GameMap.MAX_DIST,TargetVisibility.VISIBLE,1,0);
        SimpleEffect vulc11 = new SquareDamageEffect(0,Game.MAXPLAYERS,1,1,TargetVisibility.VISIBLE,1,1,false,false);

//
//        //Heatseeker
//        SimpleEffect rock01 = new SimpleEffect (3,0,1,1,false,false,0,0,0,0,1,12,false,false,false,0,false,false,Target.PLAYER);
        SimpleEffect rock01 = new PlainDamageEffect(1,1,0, GameMap.MAX_DIST,TargetVisibility.INVISIBLE,3,0,false,DifferentTarget.ANYONE,false,false);

//
//        //Hellion
//        SimpleEffect flas01 = new SimpleEffect (1,0,1,1,false,false,0,0,1,2,1,12,false,false,false,0,false,false, Target.PLAYER);
//        SimpleEffect flas02 = new SimpleEffect (0,1,1,1,false,false,0,0,1,2,0,0,true,false,false,0,false,false,Target.SQUARE);
//        // stesso di flas01 -- SimpleEffect flas11 = new SimpleEffect (1,0,1,1,false,false,0,0,1,2,1,12,false,false,false,0,false,false, Target.PLAYER);
//        SimpleEffect flas12 = new SimpleEffect (0,2,1,1,false,false,0,0,1,2,0,0,true,false,false,0,false,false,Target.SQUARE);
        SimpleEffect flash01 = new PlainDamageEffect(1,1,1, GameMap.MAX_DIST,TargetVisibility.VISIBLE,1,0,false,DifferentTarget.ANYONE,false,false);
        SimpleEffect flash02 = new SquareDamageEffect(0,Game.MAXPLAYERS,0,0,TargetVisibility.VISIBLE,0, 1,true,false);
        SimpleEffect flash11 = new PlainDamageEffect(1,1,1, GameMap.MAX_DIST,TargetVisibility.VISIBLE,1,0,false,DifferentTarget.ANYONE,false,false);
        SimpleEffect flash12 = new SquareDamageEffect(0,Game.MAXPLAYERS,0,0,TargetVisibility.VISIBLE,0, 2,true,false);

//
//        //Flamethrower
//        SimpleEffect flam01 = new SimpleEffect (1,0,1,1,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.PLAYER);
//        SimpleEffect flam02 = new SimpleEffect (1,0,1,1,false,false,0,0,1,2,1,1,true,false,false,0,true,false,Target.PLAYER);
//        SimpleEffect flam11 = new SimpleEffect (2,0,1,1,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.PLAYER);
//        SimpleEffect flam12 = new SimpleEffect (1,0,1,1,false,false,0,0,1,2,1,1,true,false,false,0,true,false,Target.PLAYER);
        SimpleEffect flam01 = new PlainDamageEffect(1,1,1,1,TargetVisibility.VISIBLE,1,0,false,DifferentTarget.ANYONE,false,false);
        SimpleEffect flam02 = new PlainDamageEffect(0,1,1,1,TargetVisibility.VISIBLE,1,0,false,DifferentTarget.ANYONE,true,true);
        SimpleEffect flam11 = new SquareDamageEffect(1,1,1,1,TargetVisibility.VISIBLE,2,0,false,false);
        SimpleEffect flam12 = new SquareDamageEffect(0,1,1,1,TargetVisibility.VISIBLE,1,0,true,true);

//
//        //Granade launcher
//        SimpleEffect gran01 = new SimpleEffect (1,0,1,1,true,false,0,1,1,2,0,12,false,false,false,0,false,false,Target.PLAYER);
//        SimpleEffect gran21 = new SimpleEffect (1,0,1,1,false,false,0,0,1,2,0,12,false,false,false,0,false,true,Target.SQUARE);
        SimpleEffect gran01 =  new PlainDamageEffect(1,1,0, GameMap.MAX_DIST,TargetVisibility.VISIBLE,1,0,false,DifferentTarget.ANYONE,false,false);
        SimpleEffect gran02 = new MovementEffect(0,1,0, GameMap.MAX_DIST,0,1,TargetVisibility.VISIBLE,false,TargetVisibility.EVERYWHERE,false,false,true,false, DifferentTarget.ANYONE);
        SimpleEffect gran21 = new SquareDamageEffect(0,Game.MAXPLAYERS,0, GameMap.MAX_DIST,TargetVisibility.VISIBLE,1,0,false,false);
        //gran21 deve poter essere fatto prima dell'effetto base
//
//        //Rocket launcher
//        SimpleEffect lcrz01 = new SimpleEffect (2,0,1,1,true,false,0,1,1,2,1,12,false,false,false,0,false,false,Target.PLAYER);
//        SimpleEffect lcrz11 = new SimpleEffect (3, 0,1,1,true,false,0,1,1,2,1,12,false,false,false,0,false,false,Target.PLAYER);
//        SimpleEffect lcrz12 = new SimpleEffect (1,0,1,1,false,false,0,0,1,2,0,0,true,false,false,2,false,false,Target.SQUARE);
//        SimpleEffect lcrz21 = new SimpleEffect (0,0,0,0,false,true,1,2,1,2,0,0,false,false,false,0,false,true,Target.PLAYER);
        SimpleEffect lcrz01 = new PlainDamageEffect(1,1,1, GameMap.MAX_DIST, TargetVisibility.VISIBLE,2,0,false,DifferentTarget.ANYONE,false,false);
        SimpleEffect lcrz02 = new MovementEffect(0,1,1, 1,0,1,TargetVisibility.VISIBLE,false,TargetVisibility.EVERYWHERE,false,false,true,false,DifferentTarget.ANYONE);
        SimpleEffect lcrz21 = new MovementEffect(1,1,0,0,1,2,TargetVisibility.VISIBLE,true,TargetVisibility.EVERYWHERE,false,false,false,false,DifferentTarget.ANYONE);
        SimpleEffect lcrz11 = new SquareDamageEffect(1,Game.MAXPLAYERS,1,Game.MAXPLAYERS,TargetVisibility.VISIBLE,1,0,false,false);
        SimpleEffect lcrz12 = new PlainDamageEffect(1,1,0,0,TargetVisibility.VISIBLE,2,0,false,DifferentTarget.ANYONE,true,false);
        SimpleEffect lcrz13 = new MovementEffect(0,1,1, 1,0,1,TargetVisibility.VISIBLE,false,TargetVisibility.EVERYWHERE,false,false,true,false,DifferentTarget.ANYONE);
//
//        //Laser (railgun)
//        SimpleEffect lsr01 = new SimpleEffect (3,0,1,1, false,false,0,0,2,2,0,12,false,false,false,0,false,false,Target.DIRECTION);
//        SimpleEffect lsr11 = new SimpleEffect (2,0,1,2,false,false,0,0,2,2,0,12,false,false,false,0,true,false,Target.DIRECTION);
        SimpleEffect lsr01 = new PlainDamageEffect(1,1,0, GameMap.MAX_DIST,TargetVisibility.DIRECTION,3,0,false,DifferentTarget.ANYONE,false,false);
        SimpleEffect lsr11 = new PlainDamageEffect(1,1,0, GameMap.MAX_DIST,TargetVisibility.DIRECTION,3,0,false,DifferentTarget.ANYONE,false,false);
        SimpleEffect lsr12 = new PlainDamageEffect(0,1,0, GameMap.MAX_DIST,TargetVisibility.EVERYWHERE,3,0,false,DifferentTarget.ANYONE,false,true);


//
//        //Photonic blade (cyberblade)
//        SimpleEffect phot01 = new SimpleEffect (2,0,1,1,false,false,0,0,1,2,0,0,false,false,false,0,false,false,Target.PLAYER);
//        SimpleEffect phot21 = new SimpleEffect (2,0,1,1,false,false,0,0,1,2,0,0,false,false,false,1,false,false,Target.PLAYER);
//        SimpleEffect phot32 = new SimpleEffect (0,0,0,0,false,true,0,1,1,2,0,0,false,false,false,0,false,false,Target.PLAYER);
        SimpleEffect phot01 = new PlainDamageEffect(1,1,0,0,TargetVisibility.VISIBLE,2,0,false,DifferentTarget.ANYONE,false,false);
        SimpleEffect phot21 = new MovementEffect(1,1,0,0,1,1,TargetVisibility.VISIBLE,true,TargetVisibility.VISIBLE,false,false,false,false,DifferentTarget.ANYONE);
        SimpleEffect phot31 = new PlainDamageEffect(1,1,0,0,TargetVisibility.VISIBLE,2,0,false, DifferentTarget.NONEOFTHEPREVIOUS,false,false);

//
//        //ZX-2
//        SimpleEffect zx01 = new SimpleEffect (1,2,1,1,false,false,0,0,1,2,0,12,false,false,false,0,false,false,Target.PLAYER);
//        SimpleEffect zx11 = new SimpleEffect(0,1,0,3,false,false,0,0,1,2,0,12,false,false,false,0,false,false,Target.PLAYER);
        SimpleEffect zx01 = new PlainDamageEffect(1,1,0, GameMap.MAX_DIST,TargetVisibility.VISIBLE,2,1,false,DifferentTarget.ANYONE,false,false);
        SimpleEffect zx11 = new PlainDamageEffect(1,3,0, GameMap.MAX_DIST,TargetVisibility.VISIBLE,0,1,false, DifferentTarget.ANYONE,false,false);
//
//        //Pump (shotgun)
//        SimpleEffect pmp01 = new SimpleEffect (3,0,1,1,true,false,0,1,1,2,0,0,false,false,false,0,false,false,Target.PLAYER);
//        SimpleEffect pmp11 = new SimpleEffect (2,0,1,1,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.PLAYER);
            SimpleEffect pmp01 = new PlainDamageEffect(1,1,0,0,TargetVisibility.VISIBLE,3,0,false,DifferentTarget.ANYONE,false,false);
            SimpleEffect pmp02 = new MovementEffect(0,1,0,1,0,1,TargetVisibility.VISIBLE,false,TargetVisibility.EVERYWHERE,false,false,false,false,DifferentTarget.ANYONE);
            SimpleEffect pmp11 = new PlainDamageEffect(1,1,1,1,TargetVisibility.VISIBLE,2,0,false,DifferentTarget.ANYONE,false,false);
//
//        //Cyber (powerglove)
//        //effetto cyb14 solo se fatto cyb13 (CORRETTO)
        SimpleEffect cyb01 = new PlainDamageEffect(1,1,1,1,TargetVisibility.VISIBLE,1,2,false,DifferentTarget.ANYONE,false,false);
        SimpleEffect cyb02 = new MovementEffect(1,1,0,0,1,1,TargetVisibility.VISIBLE,true,TargetVisibility.EVERYWHERE,false,true,false,false,DifferentTarget.ANYONE);

        SimpleEffect cyb11 = new MovementEffect(1,1,1,1,0,1,TargetVisibility.VISIBLE,true,TargetVisibility.EVERYWHERE,false,false,false,false,DifferentTarget.ANYONE);
        SimpleEffect cyb12 = new PlainDamageEffect(0,1,0,0,TargetVisibility.VISIBLE,2,0,false,DifferentTarget.ANYONE,false,false);
        SimpleEffect cyb13 = new PlainDamageEffect(0,1,1,1,TargetVisibility.VISIBLE,2,0,false,DifferentTarget.ANYONE,false,true);
        SimpleEffect cyb14 = new MovementEffect(0,1,1,1,0,1,TargetVisibility.VISIBLE,true,TargetVisibility.EVERYWHERE,false,true,false,false,DifferentTarget.ANYONE);





//
//          Onda d'urto (shockwave) //verificare
//        SimpleEffect ond01 = new SimpleEffect (1,0,1,3,false,false,0,0,1,2,1,1,false,false,false,0,false,false,Target.PLAYER);
        SimpleEffect ond01 = new AreaDamageEffect(1,3,1,1,TargetVisibility.VISIBLE,1,0,1);

        SimpleEffect ond11 = new AreaDamageEffect(4,4,1,1,TargetVisibility.VISIBLE,1,0,Game.MAXPLAYERS);

        //SLEDGEHAMMER (martello ionico)
        SimpleEffect sled01 = new PlainDamageEffect(1,1,0,0,TargetVisibility.VISIBLE,2,0,false,DifferentTarget.ANYONE,false,false);
        SimpleEffect sled11 = new PlainDamageEffect(1,1,0,0,TargetVisibility.VISIBLE,3,0,false,DifferentTarget.ANYONE,false,false);
        SimpleEffect sled12 = new MovementEffect(1,1,0,1,0,1,TargetVisibility.VISIBLE,false,TargetVisibility.EVERYWHERE,false,false,false,false,DifferentTarget.ANYONE);
        SimpleEffect sled13 = new MovementEffect(1,1,0,1,0,1,TargetVisibility.VISIBLE,false,TargetVisibility.EVERYWHERE,false,false,false,true,DifferentTarget.ANYONE);


        /*** POWER CARD ***/
        //Targeting scope
        SimpleEffect ts01 = new PlainDamageEffect(1,1,0, GameMap.MAX_DIST,TargetVisibility.VISIBLE,1,0,true,DifferentTarget.ANYONE,false,false);

        //Newton
        SimpleEffect nw01 = new MovementEffect(1,1,0,1,0,1,TargetVisibility.EVERYWHERE,false,TargetVisibility.EVERYWHERE,false,false,false,false,DifferentTarget.ANYONE);
        SimpleEffect nw02 = new MovementEffect(0,1,0,1,0,1,TargetVisibility.EVERYWHERE,false,TargetVisibility.EVERYWHERE,false,false,true,true,DifferentTarget.ANYONE);

        //Telereporter
        SimpleEffect tr01 = new MovementEffect(0,0,0,0,1, GameMap.MAX_DIST,TargetVisibility.VISIBLE,true,TargetVisibility.EVERYWHERE,false,false,false,false,DifferentTarget.ANYONE);

        //Tagback granade
        SimpleEffect tg01 = new PlainDamageEffect(1,1,0, GameMap.MAX_DIST,TargetVisibility.VISIBLE,0,1,false,DifferentTarget.ANYONE,false,false);


        /*JAXBContext contextObj = JAXBContext.newInstance(CardWeapon.class, SimpleEffect.class);

        Marshaller marshallerObj = contextObj.createMarshaller();
        marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        FileOutputStream fo = new FileOutputStream("testEffect.xml");

        marshallerObj.marshal(cwLR, fo);
        //marshallerObj.marshal(distr21, fo);*/

    }

    /**
     * Reload an unloaded weapon without cardPower usage
     */
    @Test
    void reloadWeaponTest(){
        Player p = new Player(1, PlayerColor.GREEN);
        p.addAmmo(Color.BLUE);
        p.addAmmo(Color.BLUE);
        List<Color> priceLRB = new ArrayList<>();
        priceLRB.add(Color.BLUE);
        priceLRB.add(Color.BLUE);
        CardWeapon cwLR = new CardWeapon("Lock rifle",priceLRB,null,null,null,false,false);
        cwLR.setShooter(p);
        p.addWeapon(cwLR);
        cwLR.setLoaded(false);
        try {
            cwLR.reloadWeapon(new ArrayList<>());
        } catch (InsufficientAmmoException e) {
            e.printStackTrace();
        }
        assertTrue(cwLR.isLoaded());
    }

    /**
     * Reload an unloaded weapon with cardPower usage
     */
    @Test
    void reloadWeaponTest2(){
        Player p = new Player(1, PlayerColor.GREEN);
        p.addAmmo(Color.BLUE);
        List<Color> priceLRB = new ArrayList<>();
        priceLRB.add(Color.BLUE);
        priceLRB.add(Color.BLUE);
        CardWeapon cwLR = new CardWeapon("Lock rifle",priceLRB,null,null,null,false,false);
        cwLR.setShooter(p);
        p.addWeapon(cwLR);
        cwLR.setLoaded(false);
        try {
            cwLR.reloadWeapon(Collections.singletonList(new CardPower(1, "Targeting scope"," ", Color.BLUE, false,true, null)));
        } catch (InsufficientAmmoException e) {
            e.printStackTrace();
        }
        assertTrue(cwLR.isLoaded());
    }

    /**
     * Return if two weapons are equals
     */
    @Test
    void equalsTest(){
        List<Color> priceLRB = new ArrayList<>();
        priceLRB.add(Color.BLUE);
        priceLRB.add(Color.BLUE);
        CardWeapon cwLR = new CardWeapon("Lock rifle",priceLRB,null,null,null,false,false);
        cwLR.setId(1);
        CardWeapon cwLA = new CardWeapon("ZX-2",priceLRB,null,null,null,false,false);
        cwLA.setId(2);
        assertTrue(!cwLR.equals(cwLA));
    }
}