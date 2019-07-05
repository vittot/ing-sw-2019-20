package game.controller;

import game.model.*;
import game.model.effects.*;
import org.jdom2.DataConversionException;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Class with static methods to read XML files
 */
public class XMLParser {

    /**
     * Read the configuration file
     */
    public static void readConfigFile()
    {
        SAXBuilder builder = new SAXBuilder();
        Document document;
        try
        {
            document = builder.build(new File("config.xml"));
            Element root = document.getRootElement();
            int turnTimerMs = Integer.parseInt(root.getChildText("turnTimerMs"));
            int waitingRoomTimerMs = Integer.parseInt(root.getChildText("waitingRoomTimerMs"));
            int pingIntervalMs = Integer.parseInt(root.getChildText("pingIntervalMs"));
            int pingWaitingTimeMs = Integer.parseInt(root.getChildText("pingWaitingTimeMs"));
            Configuration.TURN_TIMER_MS = turnTimerMs;
            Configuration.WAITING_ROOM_TIMER_MS = waitingRoomTimerMs;
            Configuration.PING_WAITING_TIME_MS = pingWaitingTimeMs;
            Configuration.PING_INTERVAL_MS = pingIntervalMs;

        } catch (Exception e) {
            System.out.println("Error while reading config file, default values loaded");
        }
    }

    /**
     * Write the default configuration file
     */
    public static void writeDefaultConfigFile() {
        File f = new File("config.xml");
        try (PrintWriter pw = new PrintWriter(f)){
            pw.println("<config>");
            pw.println("    <turnTimerMs>300001</turnTimerMs>");
            pw.println("    <waitingRoomTimerMs>10001</waitingRoomTimerMs>");
            pw.println("    <pingIntervalMs>10001</pingIntervalMs>");
            pw.println("    <pingWaitingTimeMs>4001</pingWaitingTimeMs>");
            pw.println("</config>");
        } catch (FileNotFoundException e) {
            System.out.println("Error during default file writing");
        }

    }

    /**
     * Read maps
     * @return the available maps list
     */
    public static List<GameMap> getAvailableMaps()
    {
        int id = 1;
        List<GameMap> availableMaps = new ArrayList<>();
        GameMap m;
        do{
            m = readMap(id,"mapFile.xml");
            if(m != null)
                availableMaps.add(m);
            id++;
        }while(m != null);

        return availableMaps;
    }

    /**
     * Read the xml map
     * @param id int
     * @param fileName string
     * @return gamemap
     */
    public static GameMap readMap(int id, String fileName){
        GameMap map = new GameMap(id,4,3);;
        Square[][] grid = null;
        int x = 0;
        int y = 0;
        String desc = null;
        Edge[] edges = new Edge[4];
        int k = 0;
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        try
        {
            document = builder.build(ClassLoader.getSystemClassLoader().getResourceAsStream("XML/" + fileName));
            Element root = document.getRootElement();
            for (Element tmpMap : root.getChildren("map")){
                if(tmpMap.getAttribute("id").getIntValue() == id){
                    grid = new Square[3][4];
                    desc = tmpMap.getChildText("desc");
                    for(Element sq : tmpMap.getChildren("square")){

                        if(!sq.getChildren().isEmpty()) {
                            grid[y][x] = new Square();

                            grid[y][x].setColor(createEquivalentMapColor(sq.getChildText("color")));
                            for (Element edg : sq.getChildren("edge")) {
                                edges[k] = createEquivalentEdge(edg.getText().trim());
                                k++;
                            }
                            k = 0;
                            grid[y][x].setEdges(edges.clone());
                            grid[y][x].setRespawn(sq.getChildText("respawn").equals("true"));
                            grid[y][x].setMap(map);
                            grid[y][x].setX(x);
                            grid[y][x].setY(y);
                        }
                        if (x == 3) {
                            x = 0;
                            y++;
                        } else {
                            x++;
                        }
                    }

                }
            }


        } catch (JDOMException e1) {
            return null;
        } catch (IOException e1) {
            return null;
        }
        if(grid != null){
            map.setDescription(desc);
            map.setGrid(grid);
        }
        else{
            map = null;
        }

        return map;
    }

    /**
     * create edge from a string
     * @param name string
     * @return edge
     */
    private static Edge createEquivalentEdge (String name){
        if(name.equals("door")) return Edge.DOOR;
        if(name.equals("open")) return Edge.OPEN;
        if(name.equals("wall")) return Edge.WALL;
        return null;
    }

    /**
     * Retunr the color of the square
     * @param name String
     * @return map color
     */
    private static MapColor createEquivalentMapColor (String name){
        if(name.equals("blue")) return MapColor.BLUE;
        if(name.equals("grey")) return MapColor.GREY;
        if(name.equals("green")) return MapColor.GREEN;
        if(name.equals("purple")) return MapColor.PURPLE;
        if(name.equals("red")) return MapColor.RED;
        if(name.equals("yellow")) return MapColor.YELLOW;
        return null;
    }

    /**
     * read from xml and call the methods to build a weapon deck
     * @param fileName filename
     * @return false in case of error
     */
    public static boolean readDeck(String fileName, Game g){
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        int id=1;
        try
        {
            document = builder.build(ClassLoader.getSystemClassLoader().getResourceAsStream("XML/" + fileName));
            Element root = document.getRootElement();
            for (Element weapon : root.getChildren("weapon")){
                addWeapon(weapon,id,g);
                id++;
            }
        } catch (JDOMException e1) {
            e1.printStackTrace();
            return false;
        } catch (IOException el) {
            el.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * read from xml and call the methods to build the ammo deck
     * @param fileName filename
     * @return false in case of error
     */
    public static boolean readAmmoDeck(String fileName, Game g){
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        try
        {
            document = builder.build(ClassLoader.getSystemClassLoader().getResourceAsStream("XML/" + fileName));
            Element root = document.getRootElement();
            for (Element cardAmmo : root.getChildren("ammo"))
                addAmmoCard(cardAmmo,g);
        } catch (JDOMException e1) {
            e1.printStackTrace();
            return false;
        } catch (IOException el) {
            el.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * read from xml and call the methods to build the powerup deck
     * @param fileName fileName
     * @param g game to which the power up are added
     * @return false in case of error
     */
    public static boolean readPowerUpDeck(String fileName, Game g){
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        int id = 1;
        try
        {
            document = builder.build(ClassLoader.getSystemClassLoader().getResourceAsStream("XML/" + fileName));
            Element root = document.getRootElement();
            for (Element powerup : root.getChildren("powerUp")){
                addPowerUp(powerup,g);
                id++;
            }
        } catch (JDOMException e1) {
            e1.printStackTrace();
            return false;
        } catch (IOException el) {
            el.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * Get a Element ammo and build it
     * @param ammo
     * @param g game to which the ammo card is added
     */
    private static void addAmmoCard(Element ammo, Game g){

        List<Color> ammos = new ArrayList<>();
        for(Element e : ammo.getChildren("color")) {
            if(!e.getText().trim().equals(""))
                ammos.add(createEquivalentAmmo(e.getText().trim()));
        }
        int cardPower = Integer.parseInt(ammo.getChildText("powerup"));

        CardAmmo cardAmmo = new CardAmmo(ammos,cardPower);
        g.getDeckAmmo().add(cardAmmo);
        //Collections.shuffle(this.deckAmmo);
    }

    /**
     * Get a Element powerup and build it
     * @param powerup
     * @param g game to which the power up is added
     */
    private static void addPowerUp(Element powerup, Game g){
        String name = powerup.getChild("name").getText().trim();
        String desc = powerup.getChild("description").getText().trim();
        Color c = createEquivalentAmmo(powerup.getChild("color").getText().trim());
        List<Color> price = takePrice(powerup);
        boolean flag = (powerup.getChild("useWhenDamaged").getText().trim().equals("true"));

        FullEffect effect = takePowerUpEffect(powerup);

        effect.setPrice(price);
        effect.setName(name);
        effect.setDescription(desc);

        int id = g.getDeckPower().size() + 1;

        CardPower cardPower = new CardPower(id,name,desc,c,flag,false,effect);
        g.getDeckPower().add(cardPower);
        //Collections.shuffle(this.deckPower);
    }

    /**
     * Get a Element weapon and build it
     * @param weapon
     * @param id
     * @param g game to which the weapon is added
     */
    private static void addWeapon(Element weapon, int id, Game g){
        String name = weapon.getChild("name").getText().trim();
        List desc = takeDescription(weapon);
        List names = takeNameEffect(weapon);
        boolean plusBefore;
        boolean plusOrder;
        List<Color> price = takePrice(weapon);
        List<Color> priceal = takePriceAl(weapon);
        List<List<Color>> priceop = takePriceOpz(weapon);
        FullEffect effect = takeEffect(weapon);
        FullEffect effectal = takeEffectal(weapon);
        List<FullEffect> effectop = takeEffectopz(weapon);
        plusBefore = (weapon.getChild("plusBeforeBase").getText().trim().equals("true"));
        plusOrder = (weapon.getChild("plusOrder").getText().trim().equals("true"));
        insertDescription(effect, effectal, effectop, desc, names);
        insertPrice(effectal,effectop,priceal,priceop);
        CardWeapon wp = new CardWeapon(name, price, effect, effectop, effectal, plusBefore, plusOrder);
        wp.setId(id);
        g.getDeckWeapon().add(wp);
        //Collections.shuffle(this.deckWeapon);
    }

    /**
     * Insert the price in the weapon effect
     * @param effectal  effect
     * @param effectop effect
     * @param priceal price
     * @param priceop price
     */
    private static void insertPrice(FullEffect effectal, List<FullEffect> effectop, List<Color> priceal, List<List<Color>> priceop) {
        //effect.setPrice(price);
        if(effectal!=null)
            effectal.setPrice(priceal);
        if(effectop==null)
            return;
        int i = 0;
        for(FullEffect fe : effectop){
            if(!priceop.isEmpty() && i<priceop.size())
                fe.setPrice(priceop.get(i));
            else
                fe.setPrice(null);
            i++;
        }
    }

    /**
     * Insert the description of the effect
     * @param ef base
     * @param aef alternative
     * @param oef optional
     * @param desc description
     * @param name name
     */
    private static void insertDescription (FullEffect ef,FullEffect aef,List<FullEffect> oef, List<String> desc, List<String> name){
        ef.setName(name.get(0));
        ef.setDescription(desc.get(0));
        int i = 1;
        if(aef != null){
            aef.setName(name.get(i));
            aef.setDescription(desc.get(i));
            i++;
        }
        if(oef != null)
            for(FullEffect fe : oef){
                fe.setDescription(desc.get(i));
                fe.setName(name.get(i));
                i++;
            }
    }

    /**
     * create movement effect from xml element
     * @param effect
     * @return
     * @throws DataConversionException
     */
    private static SimpleEffect createEquivalentMovementEffect (Element effect) throws DataConversionException {
        int mine = Integer.parseInt(effect.getChildText("minEnemy"));
        int maxd = Integer.parseInt(effect.getChildText("maxDist"));
        int maxe = Integer.parseInt(effect.getChildText("maxEnemy"));
        int mind = Integer.parseInt(effect.getChildText("minDist"));
        int minm = Integer.parseInt(effect.getChildText("minMove"));
        int maxm = Integer.parseInt(effect.getChildText("maxMove"));
        TargetVisibility visib = createVisibility(effect.getChild("targetVisib").getText().trim());
        boolean moves = (effect.getChildText("moveShooter").equals("true"));
        TargetVisibility after = createVisibility(effect.getChild("targetVisibAfter").getText().trim());
        boolean mypos= (effect.getChildText("myPos").equals("true"));
        boolean chain = (effect.getChildText("chainMove").equals("true"));
        boolean last = (effect.getChildText("lastTarget").equals("true"));
        boolean same = (effect.getChildText("sameDirection").equals("true"));
        DifferentTarget diff = createDifferent(effect.getChild("differentTarget").getText().trim());
        SimpleEffect move = new MovementEffect(mine,maxe,mind,maxd,minm,maxm,visib,moves,after,mypos,chain,last,same,diff);
        return move;
    }
    /**
     * create plainDamageEffect from xml element
     * @param effect
     * @return
     * @throws DataConversionException
     */
    private static SimpleEffect createEquivalentPlainEffect (Element effect) throws DataConversionException {
        int mine = Integer.parseInt(effect.getChildText("minEnemy"));
        int mind = Integer.parseInt(effect.getChildText("minDist"));
        int dam = Integer.parseInt(effect.getChildText("damage"));
        int maxd = Integer.parseInt(effect.getChildText("maxDist"));
        TargetVisibility visib = createVisibility(effect.getChild("targetVisib").getText().trim());
        int maxe = Integer.parseInt(effect.getChildText("maxEnemy"));
        boolean last = (effect.getChildText("lastTarget").equals("true"));
        int marks = Integer.parseInt(effect.getChildText("marks"));
        DifferentTarget diff = createDifferent(effect.getChild("differentTarget").getText().trim());
        boolean chain = (effect.getChildText("chainTarget").equals("true"));
        boolean same = (effect.getChildText("sameDirection").equals("true"));
        SimpleEffect plain = new PlainDamageEffect(mine,maxe,mind,maxd,visib,dam,marks,last,diff,chain,same);
        return plain;
    }
    /**
     * create squareDamageEffect from xml element
     * @param effect
     * @return
     * @throws DataConversionException
     */
    private static SimpleEffect createEquivalentSquareEffect (Element effect) throws DataConversionException {
        int mine = Integer.parseInt(effect.getChildText("minEnemy"));
        int marks = Integer.parseInt(effect.getChildText("marks"));
        int maxe = Integer.parseInt(effect.getChildText("maxEnemy"));
        int maxd = Integer.parseInt(effect.getChildText("maxDist"));
        TargetVisibility visib = createVisibility(effect.getChild("targetVisib").getText().trim());
        int mind = Integer.parseInt(effect.getChildText("minDist"));
        int dam = Integer.parseInt(effect.getChildText("damage"));
        boolean last = (effect.getChildText("lastTargetSquare").equals("true"));
        boolean same = (effect.getChildText("sameDirection").equals("true"));
        SimpleEffect square = new SquareDamageEffect(mine,maxe,mind,maxd,visib,dam,marks,last,same);
        return square;
    }
    /**
     * create roomDamageEffect from xml element
     * @param effect
     * @return
     * @throws DataConversionException
     */
    private static SimpleEffect createEquivalentRoomEffect (Element effect) throws DataConversionException {
        int maxd = Integer.parseInt(effect.getChildText("maxDist"));
        int maxe = Integer.parseInt(effect.getChildText("maxEnemy"));
        int mind = Integer.parseInt(effect.getChildText("minDist"));
        int mine = Integer.parseInt(effect.getChildText("minEnemy"));
        int marks = Integer.parseInt(effect.getChildText("marks"));
        TargetVisibility visib = createVisibility(effect.getChild("targetVisib").getText().trim());
        int dam = Integer.parseInt(effect.getChildText("damage"));
        SimpleEffect room = new RoomDamageEffect(mine,maxe,mind,maxd,visib,dam,marks);
        return room;
    }
    /**
     * create areaDamageEffect from xml element
     * @param effect
     * @return
     * @throws DataConversionException
     */
    private static SimpleEffect createEquivalentAreaEffect (Element effect) throws DataConversionException {
        int mine = Integer.parseInt(effect.getChildText("minEnemy"));
        int maxe = Integer.parseInt(effect.getChildText("maxEnemy"));
        int mind = Integer.parseInt(effect.getChildText("minDist"));
        int maxd = Integer.parseInt(effect.getChildText("maxDist"));
        TargetVisibility visib = createVisibility(effect.getChild("targetVisib").getText().trim());
        int dam = Integer.parseInt(effect.getChildText("damage"));
        int marks = Integer.parseInt(effect.getChildText("marks"));
        int perSquare = Integer.parseInt(effect.getChildText("maxEnemyPerSquare"));
        SimpleEffect area = new AreaDamageEffect(mine,maxe,mind,maxd,visib,dam,marks,perSquare);
        return area;
    }

    /**
     * create a DifferentTarget enum from a string
     * @param vis
     * @return
     */
    private static DifferentTarget createDifferent (String vis){
        if(vis.equals("Anyone")) return DifferentTarget.ANYONE;
        if(vis.equals("NoneOfThePrevious")) return DifferentTarget.NONEOFTHEPREVIOUS;
        if(vis.equals("NotTheLast")) return DifferentTarget.NOTTHELAST;
        return null;
    }
    /**
     * create a TargetVisibility enum from a string
     * @param vis
     * @return
     */
    private static TargetVisibility createVisibility (String vis){
        if(vis.equals("Visible")) return TargetVisibility.VISIBLE;
        if(vis.equals("Invisible")) return TargetVisibility.INVISIBLE;
        if(vis.equals("Direction")) return TargetVisibility.DIRECTION;
        if(vis.equals("Everywhere")) return TargetVisibility.EVERYWHERE;
        return null;
    }

    /**
     * create a AmmoColor from a string
     * @param name
     * @return
     */
    private static Color createEquivalentAmmo(String name){
        name = name.trim();
        if(name.equals("blue"))
            return Color.BLUE;
        if(name.equals("red"))
            return Color.RED;
        if(name.equals("yellow"))
            return Color.YELLOW;
        if(name.equals("any"))
            return Color.ANY;
        return null;
    }

    /**
     * Create all the optional effects of a weapon
     * @param weapon
     * @return
     */
    private static List<FullEffect> takeEffectopz(Element weapon){
        List<FullEffect> effect = new ArrayList<FullEffect>();
        FullEffect temp = null;
        for(Element ef : weapon.getChildren("optionalEffect")) {
            temp = new FullEffect();
            for(Element efo : ef.getChildren()) {
                try {
                    if (efo.getName().equals("areaDamageEffect")) temp.addSimpleEffect(createEquivalentAreaEffect(efo));
                    if (efo.getName().equals("roomDamageEffect")) temp.addSimpleEffect(createEquivalentRoomEffect(efo));
                    if (efo.getName().equals("plainDamage")) temp.addSimpleEffect(createEquivalentPlainEffect(efo));
                    if (efo.getName().equals("movementEffect")) temp.addSimpleEffect(createEquivalentMovementEffect(efo));
                    if (efo.getName().equals("squareDamageEffect")) temp.addSimpleEffect(createEquivalentSquareEffect(efo));
                    if (efo.getName().equals("beforeBase")) temp.setBeforeBase(efo.getText().trim().equals("true"));
                } catch (DataConversionException e) {

                }
            }
            int i = 0;
            effect.add(temp);
        }
        if(effect.isEmpty())
            return null;
        return effect;
    }

    /**
     * Create all the alternative effect of a weapon
     * @param weapon
     * @return
     */
    private static FullEffect takeEffectal(Element weapon){
        FullEffect effect = new FullEffect();
        if( weapon.getChild("alternativeEffect") == null)
            return null;
        for(Element ef : weapon.getChild("alternativeEffect").getChildren()) {
            try{
                if (ef.getName().equals("roomDamageEffect")) effect.addSimpleEffect(createEquivalentRoomEffect(ef));
                if (ef.getName().equals("squareDamageEffect")) effect.addSimpleEffect(createEquivalentSquareEffect(ef));
                if (ef.getName().equals("movementEffect")) effect.addSimpleEffect(createEquivalentMovementEffect(ef));
                if (ef.getName().equals("plainDamage")) effect.addSimpleEffect(createEquivalentPlainEffect(ef));
                if (ef.getName().equals("areaDamageEffect")) effect.addSimpleEffect(createEquivalentAreaEffect(ef));
            }catch (DataConversionException e){

            }
        }
        return effect;
    }

    /**
     * Create the effect of a powerup
     * @param powerup
     * @return
     */
    private static FullEffect takePowerUpEffect(Element powerup){
        FullEffect effect = new FullEffect();
        if( powerup.getChild("effect") == null)
            return null;
        for(Element ef : powerup.getChild("effect").getChildren()) {
            try{
                if (ef.getName().equals("roomDamageEffect")) effect.addSimpleEffect(createEquivalentRoomEffect(ef));
                if (ef.getName().equals("squareDamageEffect")) effect.addSimpleEffect(createEquivalentSquareEffect(ef));
                if (ef.getName().equals("movementEffect")) effect.addSimpleEffect(createEquivalentMovementEffect(ef));
                if (ef.getName().equals("plainDamage")) effect.addSimpleEffect(createEquivalentPlainEffect(ef));
                if (ef.getName().equals("areaDamageEffect")) effect.addSimpleEffect(createEquivalentAreaEffect(ef));
            }catch (DataConversionException e){

            }
        }
        return effect;
    }

    /**
     * Create all the base effects of a weapon
     * @param weapon
     * @return
     */
    private static FullEffect takeEffect(Element weapon){
        FullEffect effect = new FullEffect();
        for(Element ef : weapon.getChild("baseEffect").getChildren()) {
            try{
                if (ef.getName().equals("plainDamage")) effect.addSimpleEffect(createEquivalentPlainEffect(ef));
                if (ef.getName().equals("squareDamageEffect")) effect.addSimpleEffect(createEquivalentSquareEffect(ef));
                if (ef.getName().equals("movementEffect")) effect.addSimpleEffect(createEquivalentMovementEffect(ef));
                if (ef.getName().equals("areaDamageEffect")) effect.addSimpleEffect(createEquivalentAreaEffect(ef));
                if (ef.getName().equals("roomDamageEffect")) effect.addSimpleEffect(createEquivalentRoomEffect(ef));
            }catch (DataConversionException e){

            }
        }
        return effect;
    }

    /**
     * take the price of the optional effect
     * @param weapon element
     * @return list of color
     */
    private static List<List<Color>> takePriceOpz(Element weapon){
        List<List<Color>> pricetot = new ArrayList<List<Color>>();
        List<Color> price;
        Color c;
        for(int i = 0; i < weapon.getChildren("optionalPrice").size() ; i ++){
            price = null;
            for (Element pr : weapon.getChildren("optionalPrice").get(i).getChildren("ammo")){
                if(createEquivalentAmmo(pr.getText().trim()) != null) {
                    if(price == null)
                        price = new ArrayList<Color>();
                    c = createEquivalentAmmo(pr.getText().trim());
                    if (c != Color.ANY)
                        price.add(c);
                }
            }
            pricetot.add(price);
        }
        return pricetot;
    }

    /**
     * take the price of the base effect
     * @param weapon element
     * @return list of color
     */
    private static List<Color> takePrice(Element weapon){
        List price = new ArrayList<Color>();
        for (Element pr : weapon.getChild("price").getChildren("ammo")){
            price.add(createEquivalentAmmo(pr.getText().trim()));
        }
        return price;
    }

    /**
     * take the alternative price of the weapon
     * @param weapon element
     * @return list of color
     */
    private static List<Color> takePriceAl(Element weapon){
        List price = null;
        if(weapon.getChild("alternativePrice") != null)
            for (Element pr : weapon.getChild("alternativePrice").getChildren("ammo")){
                if(createEquivalentAmmo(pr.getText().trim()) != null) {
                    if (price == null)
                        price = new ArrayList<Color>();
                    price.add(createEquivalentAmmo(pr.getText().trim()));
                }
            }
        return price;
    }

    /**
     * read a weapon description
     * @param weapon
     * @return
     */
    private static List<String> takeDescription(Element weapon){
        List desc = new ArrayList<String>();
        for (Element ds : weapon.getChildren("effectDescription")){
            desc.add(ds.getText().trim());
        }
        return desc;
    }

    /**
     * take the name effect
     * @param weapon elemnt
     * @return list of string
     */
    private static List<String> takeNameEffect(Element weapon){
        List eff = new ArrayList<String>();
        for (Element ds : weapon.getChildren("effectDescription")){
            eff.add(ds.getChildText("name"));
        }
        return eff;
    }
}
