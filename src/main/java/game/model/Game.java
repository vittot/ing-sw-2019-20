package game.model;

import java.util.ArrayList;
import java.util.Collections;
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
    private int killboardSize = 8;

    public Game(List<Player> players, Map map,int killBoardSize) {
        this.players = players;
        this.map = map;
        this.killBoard = new ArrayList<>(8);
        generateDecks("loadingGame.txt");
        currentTurn = new Turn();
        currentTurn.setCurrentPlayer(players.get(0));
        this.killboardSize = killBoardSize;
    }

    public List<Player> getPlayers() {
        return players;
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
    public void addKill(Player killer, Player victim, boolean isRage){
        Kill newKill = new Kill(killer,victim,isRage);
        killBoard.add(newKill);
        //TODO add record of players killed in this turn
    }

    /**
     * Shuffle the power up waste deck to reuse them
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
        deckPower.addAll(powerWaste);
        powerWaste.clear();
    }

    /**
     * Set the player for the next turn
     */
    public void changeTurn (){
        int num;
        num = players.indexOf(currentTurn.getCurrentPlayer());
        if(num + 1 == players.size()){
            currentTurn.setCurrentPlayer(players.get(0));
        }else
            currentTurn.setCurrentPlayer(players.get(num+1));
    }

    /**
     * Check if the game is terminated
     * @return true if the game is terminated
     */
    public boolean checkVictory(){
        return (killBoard.size() == killboardSize);
    }



    public List<Integer> countPoints(){
        //TODO
        return new ArrayList<>();
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