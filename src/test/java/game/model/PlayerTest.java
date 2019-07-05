package game.model;

import game.model.exceptions.InsufficientAmmoException;
import game.model.exceptions.MapOutOfLimitException;
import game.model.exceptions.NoCardWeaponSpaceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {
    private Game game;
    private CardWeapon w,w1;
    private Player p,enemy, enemy2;

    @BeforeEach
    void before()
    {
        p = new Player(1,PlayerColor.PURPLE);
        enemy = new Player(2,PlayerColor.GREEN);
        enemy2 = new Player(3,PlayerColor.GREY);
        List<Color> ammo = new ArrayList<>();
        ammo.add(Color.RED);
        ammo.add(Color.BLUE);
        ammo.add(Color.BLUE);
        ammo.add(Color.YELLOW);
        ammo.add(Color.YELLOW);
        p.setAmmo(ammo);

        List<Color> price = new ArrayList<>();
        price.add(Color.RED);
        price.add(Color.BLUE);
        price.add(Color.YELLOW);

        w = new CardWeapon(price);
        w1 = new CardWeapon(price);
        p.setWeapons(new ArrayList<CardWeapon>());

        List<CardPower> powerups = new ArrayList<>();
        powerups.add(new CardPower(1,"name","desc",Color.BLUE,false, false,null));
        powerups.add(new CardPower(2,"name","desc",Color.YELLOW,false,false,null));
        p.setCardPower(powerups);

        GameMap map = new GameMap(1,2,3);
        Square[][] grid = new Square[3][2];
        Edge[] edges1 = new Edge[]{Edge.WALL,Edge.OPEN,Edge.DOOR,Edge.WALL};
        Edge[] edges2 = new Edge[]{Edge.DOOR,Edge.WALL,Edge.OPEN,Edge.WALL};
        Edge[] edges3 = new Edge[]{Edge.OPEN,Edge.WALL,Edge.WALL,Edge.WALL};
        Edge[] edges4 = new Edge[]{Edge.WALL,Edge.WALL,Edge.DOOR,Edge.OPEN};
        Edge[] edges5 = new Edge[]{Edge.DOOR,Edge.WALL,Edge.WALL,Edge.WALL};

        grid[0][0] = new Square(MapColor.BLUE, false, 0, 0, map, edges1);
        grid[1][0] = new Square(MapColor.RED, false, 0, 1, map, edges2);
        grid[2][0] = new Square(MapColor.RED, true, 0, 2, map, edges3);
        grid[0][1] = new Square(MapColor.BLUE, true, 1, 0, map, edges4);
        grid[1][1] = new Square(MapColor.YELLOW, true, 1, 1, map, edges5);

        map.setGrid(grid);

        map.getGrid()[0][0].addPlayer(p);
        game=new Game(Collections.singletonList(p),map,8);
        p.setGame(game);
        enemy.setGame(game);
        enemy2.setGame(game);
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

    /**
     * Move the Player
     */
    @Test
    void move() {
        try {
            p.move(2, Direction.DOWN);
        }
        catch(MapOutOfLimitException p){

        }
        assertTrue(p.getPosition().getX()==0 && p.getPosition().getY()==2);
    }

    /**
     * Control that a shooter can apply a finite number of marks to an enemy
     */
    @Test
    void checkMarksNumber() {
        enemy.addThisTurnMarks(p,2);
        enemy.updateMarks();
        assertTrue(enemy.checkMarksNumber(p,2)==false);
    }

    /**
     * Control that a shooter can apply a finite number of marks to an enemy
     */
    @Test
    void findFirstDamage() {
        enemy.addDamage(p,1);
        enemy.addDamage(enemy2,1);
        assertTrue(enemy.findFirstDamage(p.getColor(),enemy2.getColor()));
    }

    /**
     * Control that a shooter can apply a finite number of marks to an enemy
     */
    @Test
    void pickUpWeapon() {
        try {
            p.pickUpWeapon(w, null, p.getCardPower());
        }
        catch(InsufficientAmmoException p){

        }
        catch(NoCardWeaponSpaceException x){
            
        }
        assertTrue(p.getWeapons().contains(w));
    }

    /**
     * Test the adding of new weapons, ammos and power-ups
     */
    @Test
    void addingItemTest(){
        int numW = p.getWeapons().size();
        p.addWeapon(w);
        int numA = p.getAmmo().size();
        p.addAmmo(Color.BLUE);
        int numCp = p.getCardPower().size();
        p.addCardPower(new CardPower(3,"name","descr",Color.BLUE,true,false,null));
        assertTrue(p.getWeapons().size() == numW+1 && p.getAmmo().size() == numA+1 && p.getCardPower().size() == numCp+1);
    }

    /**
     * Test the player respawn
     */
    @Test
    void respawnTest(){
        p.respawn(new CardPower(3,"name","descr",Color.BLUE,true,false,null));
        assertTrue(game.getMap().getSpawnpoints().contains(p.getPosition()));
    }

    /**
     * Test the player points comparing
     */
    @Test
    void pointComparingTest(){
        Player p2 = new Player(2, PlayerColor.GREY);
        p.setPoints(5);
        p2.setPoints(4);
        assertTrue(p.compareTo(p2) < 0);
    }

    /**
     * Test the control on unloaded weapons
     */
    @Test
    void hasToReloadTest() throws InsufficientAmmoException {
        p.addWeapon(w);
        p.addWeapon(w1);
        p.pay(Collections.singletonList(Color.BLUE),new ArrayList());
        w1.setLoaded(true);
        List<CardWeapon> list = p.hasToReload();
        assertTrue(!list.isEmpty() && p.canReloadWeapon(list.get(0)));
    }

    /**
     * Test dealt of damage and marks
     */
    @Test
    void addDamageTest(){
        Player p2 = new Player(2, PlayerColor.GREY);
        p.addDamage(p2,3);
        p.addThisTurnMarks(p2,1);
        assertTrue(p.getDamage().size() == 3 && p.getThisTurnMarks().size() == 1);
    }
}