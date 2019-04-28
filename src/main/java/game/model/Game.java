package game.model;

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
