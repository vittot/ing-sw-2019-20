package game.model;


import java.io.IOException;
import java.util.List;

import game.controller.GameManager;
import game.model.effects.*;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.SortedMap;

/**
 * Class that contain all the game information
 */
public class Game {
    /**
     * Id of the game
     */
    private int id;
    /**
     * List of the player in game
     */
    private List <Player> players;
    /**
     * map of the game
     */
    private GameMap map;
    /**
     * deck of all card power in the game
      */
    private List<CardPower> deckPower;
    /**
     * List of all card ammo in the agem
     */
    private List<CardAmmo> deckAmmo;
    /**
     * Listo of all card weapons in the game
     */
    private List<CardWeapon> deckWeapon;
    /**
     * List of card power wasted in the game
     */
    private List<CardPower> powerWaste;
    /**
     * List of ammo card wasted in the game
     */
    private List<CardAmmo> ammoWaste;
    /**
     * List of kill in a turn
     */
    private List<Kill> thisTurnKill;
    /**
     * List of kill in alll game
     */
    private List<Kill> killBoard;
    /**
     * First Player to play
     */
    private Player firstPlayerToPlay;
    /**
     * Current turn
     */
    private Turn currentTurn;
    /**
     * max number of player
     */
    static final int MAXPLAYERS = 5;
    /**
     * List of point for deaths
     */
    private static final List<Integer> POINTSCOUNT;
    /**
     * Max number of kill
     */
    private int killboardSize = 8;
    /**
     * List  of game listener
     */
    private List<GameListener> gameObservers;
    /**
     * number of player to be respawned
     */
    private int nPlayerToBeRespawned;
    /**
     * flag to say if the game is finished
     */
    private boolean ended;

    /**
     * Inizialize the point array
     */
    static {
        POINTSCOUNT = new ArrayList<>();
        POINTSCOUNT.add(8);
        POINTSCOUNT.add(6);
        POINTSCOUNT.add(4);
        POINTSCOUNT.add(2);
        POINTSCOUNT.add(1);
        POINTSCOUNT.add(1);
    }

    /**
     * Constructor
     * @param killBoardSize int
     */
    public Game(int killBoardSize){
        this.killboardSize = killBoardSize;
        this.ended = false;
        this.gameObservers = new ArrayList<>();
        this.killBoard = new ArrayList<>(killBoardSize);
        this.thisTurnKill = new ArrayList<>();
        this.deckWeapon = new ArrayList<>();
        this.deckAmmo = new ArrayList<>();
        this.deckPower = new ArrayList<>();
        this.ammoWaste = new ArrayList<>();
        this.powerWaste =  new ArrayList<>();
    }

    /**
     * Constructor
     * @param id int
     * @param mapId int
     * @param killBoardSize int
     * @param players list of player
     */
    public Game(int id, int mapId, int killBoardSize, List<Player> players)
    {
        this(killBoardSize);
        this.id = id;
        this.players = players;
        currentTurn = new Turn(this.players.get(0),this);
        this.firstPlayerToPlay = this.players.get(0);
        players.stream().forEach(p -> p.setGame(this));
        readDeck("effectFile.xml");
        this.map = new GameMap(GameManager.get().getMap(mapId));
        readPowerUpDeck("powerupFile.xml");
        readAmmoDeck("ammoFile.xml");
        Collections.shuffle(deckAmmo);
        Collections.shuffle(deckPower);
        Collections.shuffle(deckWeapon);
        currentTurn = new Turn(this.players.get(0),this);

    }

    /**
     *
     * @return first player to play
     */
    Player getFirstPlayerToPlay() {
        return firstPlayerToPlay;
    }

    /**
     * Constructor
     * @param players list of pllayer
     * @param map map
     * @param killBoardSize int
     */
    public Game(List<Player> players, GameMap map, int killBoardSize) {
        this(killBoardSize);
        this.players = players;
        this.map = map;
        currentTurn = new Turn(this.players.get(0),this);
        players.stream().forEach(p -> p.setGame(this));
    }

    /**
     * Constructor
     * @param players list of player
     * @param mapId int
     * @param killBoardSize int
     */
    public Game(List<Player> players, int mapId,int killBoardSize) {
        this.players = players;
        this.killboardSize = killBoardSize;
        this.killBoard = new ArrayList<>(killBoardSize);
        currentTurn = new Turn(this.players.get(0),this);
        players.stream().forEach(p -> p.setGame(this));
        readDeck("effectFile.xml");
        readMap(mapId, "mapFile.xml");
    }

    /**
     *
     * @return id of the game
     */
    public int getId() {
        return id;
    }

    /**
     *
     * @return the available map
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
        Square [][] grid = null;
        int x = 0;
        int y = 0;
        String desc = null;
        Edge [] edges = new Edge[4];
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
     * @param fileName
     * @return
     */
    public boolean readDeck(String fileName){
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        int id=1;
        try
        {
            document = builder.build(ClassLoader.getSystemClassLoader().getResourceAsStream("XML/" + fileName));
            Element root = document.getRootElement();
            for (Element weapon : root.getChildren("weapon")){
                addWeapon(weapon,id);
                id++;
            }
        } catch (JDOMException e1) {
            e1.printStackTrace();
            //TODO ecc
            return false;
        } catch (IOException el) {
            el.printStackTrace();
            //TODO ecc
            return false;
        }
        return true;
    }

    /**
     * read from xml and call the methods to build the ammo deck
     * @param fileName
     * @return
     */
    public boolean readAmmoDeck(String fileName){
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        try
        {
            document = builder.build(ClassLoader.getSystemClassLoader().getResourceAsStream("XML/" + fileName));
            Element root = document.getRootElement();
            for (Element cardAmmo : root.getChildren("ammo"))
                addAmmoCard(cardAmmo);
        } catch (JDOMException e1) {
            e1.printStackTrace();
            //TODO ecc
            return false;
        } catch (IOException el) {
            el.printStackTrace();
            //TODO ecc
            return false;
        }
        return true;
    }

    /**
     * read from xml and call the methods to build the powerup deck
     * @param fileName
     * @return
     */
    public boolean readPowerUpDeck(String fileName){
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        int id = 1;
        try
        {
            document = builder.build(ClassLoader.getSystemClassLoader().getResourceAsStream("XML/" + fileName));
            Element root = document.getRootElement();
            for (Element powerup : root.getChildren("powerUp")){
                addPowerUp(powerup);
                id++;
            }
        } catch (JDOMException e1) {
            e1.printStackTrace();
            //TODO ecc
            return false;
        } catch (IOException el) {
            el.printStackTrace();
            //TODO ecc
            return false;
        }
        return true;
    }

    /**
     * Get a Element powerup and build it
     * @param ammo
     */
    private void addAmmoCard(Element ammo){

        List<Color> ammos = new ArrayList<>();
        for(Element e : ammo.getChildren("color")) {
            if(!e.getText().trim().equals(""))
                ammos.add(createEquivalentAmmo(e.getText().trim()));
        }
        int cardPower = Integer.parseInt(ammo.getChildText("powerup"));

        CardAmmo cardAmmo = new CardAmmo(ammos,cardPower);
        this.deckAmmo.add(cardAmmo);
        //Collections.shuffle(this.deckAmmo);
    }

    /**
     * Get a Element powerup and build it
     * @param powerup
     */
    private void addPowerUp(Element powerup){
        String name = powerup.getChild("name").getText().trim();
        String desc = powerup.getChild("description").getText().trim();
        Color c = createEquivalentAmmo(powerup.getChild("color").getText().trim());
        List<Color> price = takePrice(powerup);
        boolean flag = (powerup.getChild("useWhenDamaged").getText().trim().equals("true"));

        FullEffect effect = takePowerUpEffect(powerup);

        effect.setPrice(price);
        effect.setName(name);
        effect.setDescription(desc);

        int id = this.deckPower.size() + 1;

        //TODO: read (and add on power up xml) the useWhenAttacking flag
        CardPower cardPower = new CardPower(id,name,desc,c,flag,false,effect);
        this.deckPower.add(cardPower);
        //Collections.shuffle(this.deckPower);
    }

    /**
     * Get a Element weapon and build it
     * @param weapon
     * @param id
     */
    private void addWeapon(Element weapon, int id){
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
        this.deckWeapon.add(wp);
        //Collections.shuffle(this.deckWeapon);
    }

    /**
     * Insert the price in the weapon effect
     * @param effectal  effect
     * @param effectop effect
     * @param priceal price
     * @param priceop price
     */
    private void insertPrice(FullEffect effectal, List<FullEffect> effectop, List<Color> priceal, List<List<Color>> priceop) {
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
     * Insert the description of the effetc
     * @param ef base
     * @param aef alternative
     * @param oef optional
     * @param desc description
     * @param name nanem
     */
    private void insertDescription (FullEffect ef,FullEffect aef,List<FullEffect> oef, List<String> desc, List<String> name){
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
    private SimpleEffect createEquivalentMovementEffect (Element effect) throws DataConversionException {
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
    private SimpleEffect createEquivalentPlainEffect (Element effect) throws DataConversionException {
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
    private SimpleEffect createEquivalentSquareEffect (Element effect) throws DataConversionException {
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
    private SimpleEffect createEquivalentRoomEffect (Element effect) throws DataConversionException {
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
    private SimpleEffect createEquivalentAreaEffect (Element effect) throws DataConversionException {
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
    private DifferentTarget createDifferent (String vis){
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
    private TargetVisibility createVisibility (String vis){
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
    private Color createEquivalentAmmo(String name){
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
     * Create all the optional effect of a weapon
     * @param weapon
     * @return
     */
    private List<FullEffect> takeEffectopz(Element weapon){
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
                    //TODO eccezione
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
    private FullEffect takeEffectal(Element weapon){
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
                //TODO eccezione
            }
        }
        return effect;
    }

    /**
     * Create the effect of a powerup
     * @param powerup
     * @return
     */
    private FullEffect takePowerUpEffect(Element powerup){
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
                //TODO eccezione
            }
        }
        return effect;
    }

    /**
     * Create all the base effect of a weapon
     * @param weapon
     * @return
     */
    private FullEffect takeEffect(Element weapon){
        FullEffect effect = new FullEffect();
        for(Element ef : weapon.getChild("baseEffect").getChildren()) {
            try{
                if (ef.getName().equals("plainDamage")) effect.addSimpleEffect(createEquivalentPlainEffect(ef));
                if (ef.getName().equals("squareDamageEffect")) effect.addSimpleEffect(createEquivalentSquareEffect(ef));
                if (ef.getName().equals("movementEffect")) effect.addSimpleEffect(createEquivalentMovementEffect(ef));
                if (ef.getName().equals("areaDamageEffect")) effect.addSimpleEffect(createEquivalentAreaEffect(ef));
                if (ef.getName().equals("roomDamageEffect")) effect.addSimpleEffect(createEquivalentRoomEffect(ef));
            }catch (DataConversionException e){
                //TODO eccezione
            }
        }
        return effect;
    }

    /**
     * take the price of the optional effect
     * @param weapon element
     * @return list of color
     */
    private List<List<Color>> takePriceOpz(Element weapon){
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
    private List<Color> takePrice(Element weapon){
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
    private List<Color> takePriceAl(Element weapon){
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
    private List<String> takeDescription(Element weapon){
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
    private List<String> takeNameEffect(Element weapon){
        List eff = new ArrayList<String>();
        for (Element ds : weapon.getChildren("effectDescription")){
            eff.add(ds.getChildText("name"));
        }
        return eff;
    }

    /**
     * Get the first blood shooter for the indicated Player
     * @param victim
     * @return PlayerColor of the shooter
     */
    private PlayerColor firstBlood(Player victim) {
        return victim.getDamage().get(0);
    }

    /**
     * Add the points to players after a kill, based on first blood and damage dealt
     * @param victim killed Player
     * @param countFirstBlood indicate if the first blood has to be count
     */
    public void updatePoints(Player victim, boolean countFirstBlood) {
        HashMap<PlayerColor, Integer> damagePlayer = new HashMap<>();

        for (int i = 0; i < players.size(); i++) {
            damagePlayer.put(players.get(i).getColor(), 0);
        }

        PlayerColor firstBlood;
        int countDeaths = 0;
        int numDamage = 0;
        //HashMap<PlayerColor, Integer> sorted;
        PlayerColor[] colors;
        numDamage = victim.getDamage().size();
        if (victim.getDamage().size() > 12) {
            numDamage = 12;
        }
        for (int i = 0; i < numDamage; i++) {
            damagePlayer.replace(victim.getDamage().get(i), damagePlayer.get(victim.getDamage().get(i)) + 1);
        }

        //Remove players which have zero damage on this victim
        List<PlayerColor> toBeRemoved = new ArrayList<>();
        for(Map.Entry<PlayerColor,Integer> e : damagePlayer.entrySet())
            if(e.getValue() == 0)
                toBeRemoved.add(e.getKey());

        toBeRemoved.forEach( c -> damagePlayer.remove(c));

        /*sorted = damagePlayer
                .entrySet()
                .stream()
                .sorted(Comparator.comparingInt(java.util.Map.Entry::getValue))
                .collect(Collectors.toMap(java.util.Map.Entry::getKey, java.util.Map.Entry::getValue, (n, m) -> n, HashMap::new));*/

        //Sorts player colors by damage (descending order)
        List<Map.Entry<PlayerColor,Integer>> sortedList = new ArrayList<>(damagePlayer.entrySet());
        sortedList.sort(Map.Entry.comparingByValue());
        Collections.reverse(sortedList);

        countDeaths = victim.getDeaths();

        colors = new PlayerColor[sortedList.size()];

        Map<PlayerColor,Integer> sortedMap = new LinkedHashMap<>();
        for (Map.Entry<PlayerColor,Integer> entry : sortedList) {
            sortedMap.put(entry.getKey(), entry.getValue());
        }

        for(int j=0;j<colors.length;j++)
        {
            colors[j] = sortedList.get(j).getKey();
        }

        //colors =  sortedMap.keySet().stream().toArray(PlayerColor[]::new);
        for (int i = 1; i < colors.length; i++) {
            if (sortedMap.get(colors[i - 1]).equals(sortedMap.get(colors[i]))) {
                if (!victim.findFirstDamage(colors[i - 1], colors[i])) {
                    //swap
                    firstBlood = colors[i];
                    colors[i] = colors[i-1];
                    colors[i-1] = firstBlood;
                }
            }
        }
        firstBlood = firstBlood(victim);
        for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < players.size(); j++) {
                if (colors[i] == players.get(j).getColor()) {
                    if (countDeaths >= 5) countDeaths = 5;
                    if (countDeaths == 0) countDeaths = 1;
                    if (firstBlood == colors[i] && countFirstBlood) {
                        players.get(j).addPoints(POINTSCOUNT.get(countDeaths-1) + 1);
                    } else
                        players.get(j).addPoints(POINTSCOUNT.get(countDeaths-1));
                    if (countDeaths < 5) countDeaths++;

                }

            }

        }
    }

    /**
     * Get the last kill of the given Player
     * @param player
     * @return
     */
    public Kill getLastKill(Player player) {
        return thisTurnKill.stream().filter(k -> k.getVictim() == player).reduce((f, s) -> s).orElse(null);
    }


    /**
     * Return the player with the given id
     * @param id
     * @return
     */
    public Player getPlayer(int id)
    {
        return players.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
    }

    /**
     * Add power up to the waste deck
     * @param c power up
     */
    public void addPowerWaste(CardPower c)
    {
        powerWaste.add(c);
    }

    /**
     * decrese the respawn counter
     */
    public void decreaseToBeRespawned(){if(nPlayerToBeRespawned > 0) nPlayerToBeRespawned--;}

    /**
     * Get the player to respawn
     * @return
     */
    public int getnPlayerToBeRespawned() {return nPlayerToBeRespawned; }

    /**
     * Add new player to the game
     * @param p Player to be added
     */
    public void addNewPlayer(Player p){
        if(this.players.size()<MAXPLAYERS)
            this.players.add(p);
    }

    /**
     * Add a kill to the turn temporary killboard
     * @param killer
     * @param victim
     * @param isRage
     */
    public void addThisTurnKill(Player killer, Player victim, boolean isRage) {
        Kill newKill = new Kill(killer, victim, isRage);
        thisTurnKill.add(newKill);
        notifyDeath(newKill);
    }

    /**
     * Add the kill number
     */
    private void addKill() {
        for(Kill k : thisTurnKill) {
            killBoard.add(k);
            updatePoints(k.getVictim(),!currentTurn.isFinalFrenzy());
        }
        if(thisTurnKill.size()>1)
            thisTurnKill.get(0).getKiller().addPoints(1);
        this.players.forEach(Player::notifyPoints);
        thisTurnKill.clear();
    }

    /**
     * Shuffle the ammo waste deck to reuse them
     */
    public void replaceAmmoDeck() {
        Collections.shuffle(ammoWaste);
        deckAmmo.clear();
        deckAmmo.addAll(ammoWaste);
        ammoWaste.clear();
    }

    /**
     * Shuffle the power up waste deck to reuse them
     */
    public void replacePowerUpDeck(){
        Collections.shuffle(powerWaste);
        deckPower.clear();
        deckPower.addAll(powerWaste);
        powerWaste.clear();
    }

    /**
     * Draw a powerup card from the deck
     * @return
     */
    public CardPower drawPowerUp()
    {
        if(deckPower.size() == 0 )
            replacePowerUpDeck();
        return deckPower.remove(0);
    }

    /**
     * Set the player for the next turn and return the player to be respawned
     */
    public List<Player> changeTurn (){
        int num;
        do {
            num = players.indexOf(currentTurn.getCurrentPlayer());
            if (num + 1 == players.size()) {
                currentTurn.setCurrentPlayer(players.get(0));
            } else
                currentTurn.setCurrentPlayer(players.get(num + 1));
        }while(currentTurn.getCurrentPlayer().isSuspended());
        List<Player> toBeRespawned  = checkRespawn();
        this.nPlayerToBeRespawned = toBeRespawned.size();
        if(!this.getThisTurnKill().isEmpty())
            this.addKill();
        return toBeRespawned;
    }

    /**
     * Check if the game is terminated
     * @return true if the game is terminated
     */
    public boolean checkVictory(){
        return (killBoard.size() == killboardSize);
    }

    /**
     * Check Players dead in the turn, count points and return the player who need to be respawned
     */
    public List<Player> checkRespawn() {
        List<Player> toRespawn = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).isDead() && !players.get(i).isSuspended()) {
                //updatePoints(players.get(i));
                toRespawn.add(players.get(i));
            }
        }
        return toRespawn;
    }

    /**
     * Replace ammoCard with new cards from the ammo deck. If the deck is empty the discard ammo deck is shuffled and moved to the ammo deck.
     * Replace weapons in spawnpoints. If the weapon deck is empty no new weapon appears on the map.
     */
    public void refillMap()
    {
        CardWeapon cw;
        CardAmmo ca;
        List<Square> spawnpoints = map.getSpawnpoints();
        for(Square s : spawnpoints)
        {
            while(s.getWeapons().size() < GameMap.WEAPON_PER_SQUARE && !deckWeapon.isEmpty()) {
                cw = deckWeapon.remove(0);
                s.addWeapon(Collections.singletonList(cw));
                notifyReplaceWeapon(cw, s);
            }
        }

        List<Square> otherSquares = map.getNormalSquares();
        for(Square s : otherSquares)
        {
            if(s.getCardAmmo() == null)
            {
                //the ammo cards are all in the ammoDeck and in the ammoWaste deck
                if(deckAmmo.isEmpty())
                    replaceAmmoDeck();
                ca = deckAmmo.remove(0);
                s.setCardAmmo(ca);
                notifyReplaceAmmo(ca, s);
            }
        }

    }

    /**
     * notify of a replaced ammo
     * @param ca
     * @param s
     */
    private void notifyReplaceAmmo(CardAmmo ca, Square s) {
        gameObservers.forEach(o -> o.onReplaceAmmo(ca,s));
    }

    private void notifyReplaceWeapon(CardWeapon cw, Square spawnPoint) {
        gameObservers.forEach(o -> o.onReplaceWeapon(cw,spawnPoint));
    }

    /**
     * Notify all players about the final frenzy start
     */
    public void notifyFinalFrenzy(){
        gameObservers.forEach(o->o.onFinalFrenzy());
    }

    /**
     *  Add a new listener for game changes
     * @param gl
     */
    public void addGameListener(GameListener gl)
    {
        this.gameObservers.add(gl);
    }

    public void removeGameListener(GameListener gl)
    {
        this.gameObservers.remove(gl);
    }

    /**
     * Remove GameListener with a given username
     * @param username
     */
    public void removeGameListener(String username)
    {
        GameListener toRemove = null;
        for(GameListener gl : this.gameObservers)
            if(gl.getUsername().equals(username))
                toRemove = gl;
        this.gameObservers.remove(toRemove);
    }

    void notifyDamage(Player hit, Player attacker, int damage, int marksToRemove){
        gameObservers.forEach(o -> o.onDamage(hit,attacker,damage, marksToRemove));
    }

    void notifyMarks(Player marked, Player marker, int marks)
    {
        gameObservers.forEach(o -> o.onMarks(marked,marker,marks));
    }

    void notifyDeath(Kill kill)
    {
        gameObservers.forEach(o -> o.onDeath(kill));
    }

    void notifyGrabWeapon(Player p, CardWeapon cw, CardWeapon cww)
    {
        gameObservers.forEach(o -> o.onGrabWeapon(p,cw, cww));
    }


    void notifyGrabCardAmmo(Player p, List<Color> ammo)
    {
        gameObservers.forEach(o -> o.onGrabCardAmmo(p,ammo));
    }

    /**
     * Notify to all players the movement of a player
     * @param p
     */
    void notifyMove(Player p)
    {
        gameObservers.forEach(o -> o.onMove(p));
    }


    /**
     * Notify to all players the respawn of a player
     * @param p
     */
    void notifyRespawn(Player p){
        gameObservers.forEach(o -> o.onRespawn(p));
    }

    /**
     * Notify to all players the use of a powerup
     * @param p
     * @param c
     */
    void notifyPowerUpUse(Player p, CardPower c)
    {
        gameObservers.forEach(o -> o.onPowerUpUse(p,c));
    }

    public boolean isFinalFreazy(){
        if(killBoard.size() == killboardSize)
            return true;
        return false;
    }

    /**
     * Notify to all player that now is the turn of player p
     * @param p
     */
    void notifyTurnChange(Player p)
    {
        gameObservers.forEach( o -> o.onChangeTurn(p));
        p.notifyTurn();
    }

    /**
     * Notify all players that a player has been suspended from the game
     * @param p - Player suspended
     * @param timeOut - indicate if the suspension is due to timeout
     */
    void notifyPlayerSuspended(Player p, boolean timeOut) {
        gameObservers.forEach( o -> o.onPlayerSuspend(p, timeOut));
    }


    public void rejoinUser(String user)
    {
        for(Player p : this.getPlayers())
        {
            if(p.getNickName().equals(user))
            {
                p.rejoin();
                //return;
            }
        }
    }

    public void notifyPlayerRejoined(Player player) {
        gameObservers.forEach(o -> o.onPlayerRejoined(player));
    }

    /**
     * Stops the game, notifying all players about it
     */
    public void endGame()
    {
        this.ended = true;
        GameManager.get().endGame(this);
        gameObservers.forEach(o -> o.onGameEnd(getRanking()));
    }

    public boolean isEnded() {
        return ended;
    }

    /**
     * Return the list of players ordered by points
     * @return
     */
    public SortedMap<Player,Integer> getRanking()
    {
        SortedMap<Player,Integer> ranking = new TreeMap<>();
        ArrayList<Player> orderedPlayers = new ArrayList<>(players);
        Collections.sort(orderedPlayers);
        for(Player p : orderedPlayers)
            ranking.put(p,p.getPoints());
        return ranking;
    }

    public long getNumPlayersAlive() {
        return players.stream().filter(p -> !p.isSuspended()).count();
    }

    public void notifyUpdateMarks(Player player) {
        gameObservers.forEach(o -> o.onPlayerUpdateMarks(player));
    }

    public void notifyRage(Kill lastKill) {
        gameObservers.forEach(o -> o.onPlayerRaged(lastKill));
    }

    /**
     * Gets List of the player in game.
     *
     * @return Value of List of the player in game.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Gets deck of all card power in the game.
     *
     * @return Value of deck of all card power in the game.
     */
    public List<CardPower> getDeckPower() {
        return deckPower;
    }

    /**
     * Gets List of kill in a turn.
     *
     * @return Value of List of kill in a turn.
     */
    public List<Kill> getThisTurnKill() {
        return thisTurnKill;
    }

    /**
     * Sets new Id of the game.
     *
     * @param id New value of Id of the game.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets new List of ammo card wasted in the game.
     *
     * @param ammoWaste New value of List of ammo card wasted in the game.
     */
    public void setAmmoWaste(List<CardAmmo> ammoWaste) {
        this.ammoWaste = ammoWaste;
    }

    /**
     * Gets Current turn.
     *
     * @return Value of Current turn.
     */
    public Turn getCurrentTurn() {
        return currentTurn;
    }

    /**
     * Sets new Current turn.
     *
     * @param currentTurn New value of Current turn.
     */
    public void setCurrentTurn(Turn currentTurn) {
        this.currentTurn = currentTurn;
    }

    /**
     * Gets List of all card ammo in the agem.
     *
     * @return Value of List of all card ammo in the agem.
     */
    public List<CardAmmo> getDeckAmmo() {
        return deckAmmo;
    }

    /**
     * Gets map of the game.
     *
     * @return Value of map of the game.
     */
    public GameMap getMap() {
        return map;
    }

    /**
     * Sets new List of the player in game.
     *
     * @param players New value of List of the player in game.
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * Sets new List of all card ammo in the agem.
     *
     * @param deckAmmo New value of List of all card ammo in the agem.
     */
    public void setDeckAmmo(List<CardAmmo> deckAmmo) {
        this.deckAmmo = deckAmmo;
    }

    /**
     * Sets new map of the game.
     *
     * @param map New value of map of the game.
     */
    public void setMap(GameMap map) {
        this.map = map;
    }

    /**
     * Sets new Listo of all card weapons in the game.
     *
     * @param deckWeapon New value of Listo of all card weapons in the game.
     */
    public void setDeckWeapon(List<CardWeapon> deckWeapon) {
        this.deckWeapon = deckWeapon;
    }

    /**
     * Gets List of kill in alll game.
     *
     * @return Value of List of kill in alll game.
     */
    public List<Kill> getKillBoard() {
        return killBoard;
    }
    /**
     * Check if the killboard is full
     * @return true if killboard is full
     */
    public boolean isKillBoardFull() {
        return this.killBoard.size() >= this.killboardSize;
    }

    /**
     * Set 3 deaths to all players with 0 damage, to count correctly the points in final frenzy
     */
    public void startFinalFrenzy()
    {
        for(Player p : players)
        {
            if(p.getDamage().isEmpty())
                p.setDeaths(3);
        }
    }

    /**
     * Assign the killboard points at the end of the game
     */
    public void countKillBoardPoints() {

        HashMap<Player,Integer> kills = new HashMap<>();
        for(Kill k : killBoard)
        {
            if(kills.get(k.getKiller()) == null)
                kills.put(k.getKiller(),1);
            else
                kills.replace(k.getKiller(),kills.get(k.getKiller()),kills.get(k.getKiller())+1);
            if(k.isRage())
                kills.replace(k.getKiller(),kills.get(k.getKiller()),kills.get(k.getKiller())+1);
        }

        List<Map.Entry<Player,Integer>> sortedList = new ArrayList<>(kills.entrySet());
        sortedList.sort(Map.Entry.comparingByValue());
        Collections.reverse(sortedList);

        Map.Entry<Player,Integer> tmp;
        for(int i=0;i<sortedList.size()-1;i++)
        {
            if(sortedList.get(i).getValue() == sortedList.get(i+1).getValue())
            {
                if(!isFirstOnKillBoard(sortedList.get(i).getKey(),sortedList.get(i+1).getKey()))
                {
                    tmp = sortedList.get(i);
                    sortedList.set(i,sortedList.get(i+1));
                    sortedList.set(i+1,tmp);
                }
            }
        }

        for(int i=0;i<sortedList.size();i++)
        {
            if(i<5){
                sortedList.get(i).getKey().addPoints(POINTSCOUNT.get(i));
                sortedList.get(i).getKey().setKillboardpoints(POINTSCOUNT.get(i));
            }
            else{
                sortedList.get(i).getKey().addPoints(POINTSCOUNT.get(5));
                sortedList.get(i).getKey().setKillboardpoints(POINTSCOUNT.get(5));
            }
        }
    }

    /**
     * Check who has been the first player to put a kill on the killboard between the two parameters
     * @param p1 player one
     * @param p2 player two
     * @return true if player 1 has been the first
     */
    private boolean isFirstOnKillBoard(Player p1,Player p2)
    {
        int n1 = killboardSize + 1, n2 = killboardSize + 1;
        int i;
        for(i=0;i<killBoard.size();i++)
        {
            if(killBoard.get(i).getKiller().equals(p1))
            {
                n1 = i;
                break;
            }
        }
        for(i=0;i<killBoard.size();i++)
        {
            if(killBoard.get(i).getKiller().equals(p2))
            {
                n2 = i;
                break;
            }
        }
        return n1 < n2;
    }
}
