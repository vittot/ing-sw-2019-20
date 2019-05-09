package game.model;


import java.beans.Visibility;
import java.io.File;
import java.io.IOException;
import java.util.List;

import game.model.effects.*;
import org.jdom2.*;
import org.jdom2.input.SAXBuilder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.List;

public class Game {
    private List <Player> players;
    private Map map;
    private List<CardPower> deckPower;
    private List<CardAmmo> deckAmmo;
    private List<CardWeapon> deckWeapon;
    private List<CardPower> powerWaste;
    private List<CardAmmo> ammoWaste;
    private List<Kill> killBoard;
    private Turn currentTurn;
    public static final int MAXPLAYERS = 5;
    private static final List<Integer> POINTSCOUNT;
    private int killboardSize = 8;

    static {
        POINTSCOUNT = new ArrayList<>();
        POINTSCOUNT.add(8);
        POINTSCOUNT.add(6);
        POINTSCOUNT.add(4);
        POINTSCOUNT.add(2);
        POINTSCOUNT.add(1);
        POINTSCOUNT.add(1);
    }

    public Game(List<Player> players, Map map,int killBoardSize) {
        this.players = players;
        this.map = map;
        this.killboardSize = killBoardSize;
        this.killBoard = new ArrayList<>(killBoardSize);
        generateDecks("loadingGame.txt");
        currentTurn = new Turn(this.players.get(0),this);
        players.stream().forEach(p -> p.setGame(this));
    }
    public Game(List<Player> players, int id,int killBoardSize) {
        this.players = players;
        this.map = map;
        this.killboardSize = killBoardSize;
        this.killBoard = new ArrayList<>(killBoardSize);
        generateDecks("loadingGame.txt");
        currentTurn = new Turn(this.players.get(0),this);
        players.stream().forEach(p -> p.setGame(this));
        readDeck("effectFile.xml");
        readMap(id, "mapFile.xml");
    }


    public Map readMap(int id,String fileName){
        Map map = new Map(id,4,3);
        Square [][] grid = new Square[4][3];
        int x = 0;
        int y = 0;
        Edge [] edges = new Edge[4];
        int k = 0;
        SAXBuilder builder = new SAXBuilder();
        Document document = null;
        try
        {
            document = builder.build(fileName);
            Element root = document.getRootElement();
            for (Element tmpMap : root.getChildren("map")){
                if(tmpMap.getAttribute("id").getIntValue() == id){
                    map.setDescription(tmpMap.getChildText("desc"));
                    for(Element sq : tmpMap.getChildren("square")){
                        grid[x][y].setColor(createEquivalentMapColor(sq.getChildText("color")));
                        for(Element edg : sq.getChildren("edge")){
                            edges[k] = createEquivalentEdge(edg.getText());
                        }
                        k = 0;
                        grid[x][y].setEdges(edges);
                        grid[x][y].setRespawn(sq.getChildText("respown").equals("true"));
                        if(x == 3){
                            x = 0;
                            y++;
                        }else{
                            x++;
                        }
                    }

                }
            }


        } catch (JDOMException e1) {
            //TODO ecce
        } catch (IOException e1) {
            //TODO ecc
        }
        map.setGrid(grid);
        return map;
    }

    public Edge createEquivalentEdge (String name){
        if(name.equals("door")) return Edge.DOOR;
        if(name.equals("open")) return Edge.OPEN;
        if(name.equals("wall")) return Edge.WALL;
        return null;
    }

    public MapColor createEquivalentMapColor (String name){
        if(name.equals("blue")) return MapColor.BLUE;
        if(name.equals("grey")) return MapColor.GREY;
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
        try
        {
            document = builder.build(fileName);
            Element root = document.getRootElement();
            for (Element weapon : root.getChildren("weapon")) addWeapon(weapon);
        } catch (JDOMException e1) {
            //TODO ecce
        } catch (IOException e1) {
            //TODO ecc
        }
        return true;
    }

    /**
     * Get a Element weapon and build it
     * @param weapon
     */
    public void addWeapon(Element weapon){
        String name = weapon.getChild("name").getText();
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
        plusBefore = (weapon.getChild("plusBeforeBase").getText().equals("true"));
        plusOrder = (weapon.getChild("plusOrdere").getText().equals("true"));
        insertDescription(effect, effectal, effectop, desc, names);
        insertPrice(effect,effectal,effectop,price,priceal,priceop);
        CardWeapon wp = new CardWeapon(name, price, effect, effectop, effectal, plusBefore, plusOrder);
        this.deckWeapon.add(wp);
    }

    private void insertPrice(FullEffect effect, FullEffect effectal, List<FullEffect> effectop, List price, List<Color> priceal, List<List<Color>> priceop) {
        effect.setPrice(price);
        if(effectal!=null)
            effectal.setPrice(priceal);
        if(effectop==null)
            return;
        int i = 0;
        for(FullEffect fe : effectop){
            fe.setPrice(priceop.get(i));
        }
    }

    private void insertDescription (FullEffect ef,FullEffect aef,List<FullEffect> oef, List<String> desc, List<String> name){
        ef.setName(name.get(0));
        ef.setDescription(desc.get(0));
        int i = 1;
        if(aef != null){
            aef.setName(name.get(i));
            aef.setDescription(desc.get(i));
            i++;
        }
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
    public SimpleEffect createEquivalentMovementEffect (Element effect) throws DataConversionException {
        int mine = effect.getAttribute("minEnemy").getIntValue();
        int maxd = effect.getAttribute("maxDist").getIntValue();
        int maxe = effect.getAttribute("maxEnemy").getIntValue();
        int mind = effect.getAttribute("minDist").getIntValue();
        int minm = effect.getAttribute("minMove").getIntValue();
        int maxm = effect.getAttribute("maxMove").getIntValue();
        TargetVisibility visib = createVisibility(effect.getChild("targetVisib").getText());
        boolean moves = (effect.getChildText("moveShooter").equals("true"));
        TargetVisibility after = createVisibility(effect.getChild("targetVisibAfter").getText());
        boolean mypos= (effect.getChildText("myPos").equals("true"));
        boolean chain = (effect.getChildText("chainTarget").equals("true"));
        boolean last = (effect.getChildText("lastTargetSquare").equals("true"));
        boolean same = (effect.getChildText("sameDirection").equals("true"));
        DifferentTarget diff = createDifferent(effect.getChild("differentTarget").getText());
        SimpleEffect move = new MovementEffect(mine,maxe,mind,maxd,minm,maxm,visib,moves,after,mypos,chain,last,same,diff);
        return move;
    }
    /**
     * create plainDamageEffect from xml element
     * @param effect
     * @return
     * @throws DataConversionException
     */
    public SimpleEffect createEquivalentPlainEffect (Element effect) throws DataConversionException {
        int mine = effect.getAttribute("minEnemy").getIntValue();
        int mind = effect.getAttribute("minDist").getIntValue();
        int dam = effect.getAttribute("damage").getIntValue();
        int maxd = effect.getAttribute("maxDist").getIntValue();
        TargetVisibility visib = createVisibility(effect.getChild("targetVisib").getText());
        int maxe = effect.getAttribute("maxEnemy").getIntValue();
        boolean last = (effect.getChildText("lastTargetSquare").equals("true"));
        int marks = effect.getAttribute("marks").getIntValue();
        DifferentTarget diff = createDifferent(effect.getChild("differentTarget").getText());
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
    public SimpleEffect createEquivalentSquareEffect (Element effect) throws DataConversionException {
        int mine = effect.getAttribute("minEnemy").getIntValue();
        int marks = effect.getAttribute("marks").getIntValue();
        int maxe = effect.getAttribute("maxEnemy").getIntValue();
        int maxd = effect.getAttribute("maxDist").getIntValue();
        TargetVisibility visib = createVisibility(effect.getChild("targetVisib").getText());
        int mind = effect.getAttribute("minDist").getIntValue();
        int dam = effect.getAttribute("damage").getIntValue();
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
    public SimpleEffect createEquivalentRoomEffect (Element effect) throws DataConversionException {
        int maxd = effect.getAttribute("maxDist").getIntValue();
        int maxe = effect.getAttribute("maxEnemy").getIntValue();
        int mind = effect.getAttribute("minDist").getIntValue();
        int mine = effect.getAttribute("minEnemy").getIntValue();
        int marks = effect.getAttribute("marks").getIntValue();
        TargetVisibility visib = createVisibility(effect.getChild("targetVisib").getText());
        int dam = effect.getAttribute("damage").getIntValue();
        SimpleEffect room = new RoomDamageEffect(mine,maxe,mind,maxd,visib,dam,marks);
        return room;
    }
    /**
     * create areaDamageEffect from xml element
     * @param effect
     * @return
     * @throws DataConversionException
     */
    public SimpleEffect createEquivalentAreaEffect (Element effect) throws DataConversionException {
        int mine = effect.getAttribute("minEnemy").getIntValue();
        int maxe = effect.getAttribute("maxEnemy").getIntValue();
        int mind = effect.getAttribute("minDist").getIntValue();
        int maxd = effect.getAttribute("maxDist").getIntValue();
        TargetVisibility visib = createVisibility(effect.getChild("targetVisib").getText());
        int dam = effect.getAttribute("damage").getIntValue();
        int marks = effect.getAttribute("marks").getIntValue();
        int perSquare = effect.getAttribute("maxEnemyPerSquare").getIntValue();
        SimpleEffect area = new AreaDamageEffect(mine,maxe,mind,maxd,visib,dam,marks,perSquare);
        return area;
    }

    /**
     * create a DifferentTarget enum from a string
     * @param vis
     * @return
     */
    public DifferentTarget createDifferent (String vis){
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
    public TargetVisibility createVisibility (String vis){
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
    public Color createEquivalentAmmo(String name){
        if(name.equals("blue"))
            return Color.BLUE;
        if(name.equals("red"))
            return Color.RED;
        if(name.equals("Yellow"))
            return Color.YELLOW;
        return Color.ANY;
    }

    /**
     * Create all the optional effect of a weapon
     * @param weapon
     * @return
     */
    public List<FullEffect> takeEffectopz(Element weapon){
        List<FullEffect> effect = new ArrayList<FullEffect>();
        FullEffect temp = new FullEffect();
        for(Element ef : weapon.getChildren("optionalEffect")) {
            for(Element efo : ef.getChildren()) {
                try {
                    if (efo.getName().equals("areaDamageEffect")) temp.addSimpleEffect(createEquivalentAreaEffect(ef));
                    if (efo.getName().equals("roomDamageEffect")) temp.addSimpleEffect(createEquivalentRoomEffect(ef));
                    if (efo.getName().equals("plainDamage")) temp.addSimpleEffect(createEquivalentPlainEffect(ef));
                    if (efo.getName().equals("movementEffect")) temp.addSimpleEffect(createEquivalentMovementEffect(ef));
                    if (efo.getName().equals("squareDamageEffect")) temp.addSimpleEffect(createEquivalentSquareEffect(ef));
                } catch (DataConversionException e) {
                    //TODO eccezione
                }
            }
            int i = 0;
            effect.add(temp);
        }
        return effect;
    }

    /**
     * Create all the alternative effect of a weapon
     * @param weapon
     * @return
     */
    public FullEffect takeEffectal(Element weapon){
        FullEffect effect = new FullEffect();
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
     * Create all the base effect of a weapon
     * @param weapon
     * @return
     */
    public FullEffect takeEffect(Element weapon){
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
    public List<List<Color>> takePriceOpz(Element weapon){
        List<List<Color>> pricetot = new ArrayList<List<Color>>();
        List<Color> price = new ArrayList<Color>();
        for(int i = 0; i < weapon.getChildren("optionalPrice").size() ; i ++){
            for (Element pr : weapon.getChildren("optionalPrice").get(i).getChildren("ammo")){
                price.add(createEquivalentAmmo(pr.getText()));
            }
            pricetot.add(price);
        }
        return pricetot;
    }
    public List<Color> takePrice(Element weapon){
        List price = new ArrayList<Color>();
        for (Element pr : weapon.getChild("price").getChildren("ammo")){
            price.add(createEquivalentAmmo(pr.getText()));
        }
        return price;
    }
    public List<Color> takePriceAl(Element weapon){
        List price = new ArrayList<Color>();
        for (Element pr : weapon.getChild("alternativePrice").getChildren("ammo")){
            price.add(createEquivalentAmmo(pr.getText()));
        }
        return price;
    }

    /**
     * read a weapon description
     * @param weapon
     * @return
     */
    public List<String> takeDescription(Element weapon){
        List desc = new ArrayList<String>();
        for (Element ds : weapon.getChildren("effectDescription")){
            desc.add(ds.getText());
        }
        return desc;
    }
    public List<String> takeNameEffect(Element weapon){
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
    public PlayerColor firstBlood(Player victim) {
        return victim.getDamage().get(0);
    }

    /**
     * Add the points to players after a kill, based on first blood and damage dealt
     *
     * @param victim killed Player
     */
    public void updatePoints(Player victim) {
        HashMap<PlayerColor, Integer> damagePlayer = new HashMap<>();

        for (int i = 0; i < players.size(); i++) {
            damagePlayer.put(players.get(i).getColor(), 0);
        }

        PlayerColor firstBlood;
        int countDeaths = 0;
        int numDamage = 0;
        HashMap<PlayerColor, Integer> sorted;
        PlayerColor[] colors;
        numDamage = victim.getDamage().size();
        if (victim.getDamage().size() > 12) {
            numDamage = 12;
        }
        for (int i = 0; i < numDamage; i++) {
            damagePlayer.replace(victim.getDamage().get(i), damagePlayer.get(victim.getDamage().get(i)) + 1);
        }

        sorted = damagePlayer
                .entrySet()
                .stream()
                .sorted(Comparator.comparingInt(java.util.Map.Entry::getValue))
                .collect(Collectors.toMap(java.util.Map.Entry::getKey, java.util.Map.Entry::getValue, (n, m) -> n, HashMap::new));
        countDeaths = victim.getDeaths();
        colors =  sorted.keySet().stream().toArray(PlayerColor[]::new);
        for (int i = 1; i < colors.length; i++) {
            if (sorted.get(colors[i - 1]).equals(sorted.get(colors[i]))) {
                if (!victim.findFirstDamage(colors[i - 1], colors[i])) {
                    firstBlood = colors[i - 1];
                    colors[i] = colors[i - 1];
                    colors[i - 1] = firstBlood;
                }
            }
        }
        firstBlood = firstBlood(victim);
        for (int i = 0; i < colors.length; i++) {
            for (int j = 0; j < players.size(); j++) {
                if (colors[i] == players.get(j).getColor()) {
                    if (countDeaths >= 5) countDeaths = 5;
                    if (firstBlood == colors[i]) {
                        players.get(j).addPoints(POINTSCOUNT.get(countDeaths) + 1);
                    } else
                        players.get(j).addPoints(POINTSCOUNT.get(countDeaths));
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
        return killBoard.stream().filter(k -> k.getVictim() == player).reduce((f, s) -> s).orElse(null);
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getPlayer(int id)
    {
        return players.get(id - 1);
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public Map getMap() {
        return map;
    }

    public void setMap(Map map) {
        this.map = map;
    }

    public List<CardPower> getDeckPower() {
        return deckPower;
    }

    public void setDeckPower(List<CardPower> deckPower) {
        this.deckPower = deckPower;
    }

    public List<CardAmmo> getDeckAmmo() {
        return deckAmmo;
    }

    public void setDeckAmmo(List<CardAmmo> deckAmmo) {
        this.deckAmmo = deckAmmo;
    }

    public List<CardWeapon> getDeckWeapon() {
        return deckWeapon;
    }

    public void setDeckWeapon(List<CardWeapon> deckWeapon) {
        this.deckWeapon = deckWeapon;
    }

    public List<CardPower> getPowerWaste() {
        return powerWaste;
    }

    public void setPowerWaste(List<CardPower> powerWaste) {
        this.powerWaste = powerWaste;
    }

    public List<Kill> getKillBoard() {
        return killBoard;
    }

    public void setKillBoard(List<Kill> killBoard) {
        this.killBoard = killBoard;
    }

    public Turn getCurrentTurn() {
        return currentTurn;
    }

    public void setCurrentTurn(Turn currentTurn) {
        this.currentTurn = currentTurn;
    }

    public List<CardAmmo> getAmmoWaste() { return ammoWaste; }

    public void setAmmoWaste(List<CardAmmo> ammoWaste) { this.ammoWaste = ammoWaste; }

    /**
     * Add new player to the game
     * @param p Player to be added
     */
    public void addNewPlayer(Player p){
        if(this.players.size()<MAXPLAYERS)
            this.players.add(p);
    }

    public void generateDecks(String fileLoading){
        //TODO only one method or one for any type of card?

    }

    /**
     * Add a kill to the killboard
     * @param killer
     * @param victim
     * @param isRage
     */
    public void addKill(Player killer, Player victim, boolean isRage) {
        Kill newKill = new Kill(killer, victim, isRage);
        killBoard.add(newKill);
        updatePoints(victim);
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
        Collections.shuffle(ammoWaste);
        deckPower.clear();
        deckPower.addAll(powerWaste);
        powerWaste.clear();
    }

    /**
     * Set the player for the next turn and return the player to be respawned
     */
    public List<Player> changeTurn (){
        int num;
        num = players.indexOf(currentTurn.getCurrentPlayer());
        if(num + 1 == players.size()){
            currentTurn.setCurrentPlayer(players.get(0));
        }else
            currentTurn.setCurrentPlayer(players.get(num+1));
        return checkRespawn();
    }

    /**
     * Check if the game is terminated
     * @return true if the game is terminated
     */
    public boolean checkVictory(){
        return (killBoard.size() == killboardSize);
    }

    /**
     * Check Players dead in the turn, count points ask to respawn them
     */
    public List<Player> checkRespawn() {
        List<Player> toRespawn = new ArrayList<>();
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).isDead()) {
                updatePoints(players.get(i));
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
        List<Square> spawnpoints = map.getSpawnpoints();
        for(Square s : spawnpoints)
        {
            while(s.getWeapons().size() < Map.WEAPON_PER_SQUARE && !deckWeapon.isEmpty())
                s.addWeapon(Collections.singletonList(deckWeapon.remove(0)));
        }

        List<Square> otherSquares = map.getNormalSquares();
        for(Square s : otherSquares)
        {
            if(s.getCardAmmo() == null)
            {
                //the ammo cards are all in the ammoDeck and in the ammoWaste deck
                if(deckAmmo.isEmpty())
                    replaceAmmoDeck();
                s.setCardAmmo(deckAmmo.remove(0));
            }
        }

    }
}
