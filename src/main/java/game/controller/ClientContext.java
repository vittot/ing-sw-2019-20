package game.controller;

import game.model.Kill;

import java.util.ArrayList;
import java.util.List;
import game.model.GameMap;
import game.model.Player;
import game.model.effects.SimpleEffect;

/**
 * Model portion for the ClientNetwork
 * It's a singleton for every ClientNetwork
 */
public class ClientContext {
    /** Instance of the singleton */
    private static ClientContext instance;
    /** GameMap of the game*/
    private GameMap map;
    /** ID of the player */
    private int myID;
    /** Username of the player */
    private String user;
    /** Game killboard*/
    private List<Kill> killboard;
    /** SimpleEffect currently selected */
    private SimpleEffect currentEffect;
    /** List of players of the game which are not spawned yet*/
    private List<Player> playersInWaiting;
    /**
     * Indicate if the game is in finalFrenzy
     */
    private boolean finalFrenzy;
    /**
     * Indicated if the "movement" turn action is currently allowed
     */
    private boolean movedAllowed;

    /**
     * Default constructor
     */
    private ClientContext() {
        finalFrenzy = false;
        movedAllowed = true;
        killboard = new ArrayList<>();
    }

    /**
     * Return the instance of the singleton class but if it doesn't exist the method has to generate it before
     * @return instance
     */
    public static synchronized ClientContext get() {
        if (instance == null) {
            instance = new ClientContext();
        }

        return instance;
    }

    /**
     * Return the username of the player
     * @return username
     */
    public String getUser() {
        return user;
    }

    /**
     * Set the username of the player
     * @param user username
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * Return the Player
     * @return the player object
     */
    public Player getMyPlayer(){
        if(map.getPlayerById(myID) != null)
            return map.getPlayerById(myID);
        return playersInWaiting.stream().filter(player -> player.getId() == myID).findFirst().orElse(null);
    }

    /**
     * Return the players not spawned yet
     * @return list of players
     */
    public List<Player> getPlayersInWaiting() {
        return playersInWaiting;
    }

    /**
     * Set the list of waiting players
     * @param playersInWaiting the list of waiting players
     */
    void setPlayersInWaiting(List<Player> playersInWaiting) {
        this.playersInWaiting = playersInWaiting;
    }

    /**
     * Return the current SimpleEffect which is being applied
     * @return the simple effect
     */
    public SimpleEffect getCurrentEffect() {
        return currentEffect;
    }

    /**
     * Set the simple effect which is being applied
     * @param currentEffect the simple effect
     */
    void setCurrentEffect(SimpleEffect currentEffect) {
        this.currentEffect = currentEffect;
    }

    /**
     * Return the game map
     * @return game map
     */
    public GameMap getMap() {
        return map;
    }

    /**
     * Set the game map
     * @param map game map
     */
    public void setMap(GameMap map) {
        this.map = map;
    }

    /**
     * Return the player ID
     * @return ID
     */
    public int getMyID() {
        return myID;
    }

    /**
     * Set the player ID
     * @param myID ID
     */
    void setMyID(int myID) {
        this.myID = myID;
    }

    /**
     * Return the killboard
     * @return killboard
     */
    public List<Kill> getKillboard() {
        return killboard;
    }

    /**
     * Set the killboard
     * @param killboard killboard
     */
    public void setKillboard(List<Kill> killboard) {
        this.killboard = killboard;
    }

    /**
     * Set the finalFrenzy flag
     */
    void setFinalFrenzy() {
        this.finalFrenzy = true;
    }

    /**
     * Check if the game is in final frenzy
     * @return true if it is in final frenzy, false otherwise
     */
    public boolean isFinalFrenzy()
    {
        return finalFrenzy;
    }

    /**
     * Set the movAllowed flag
     * @param movAllowed movAllowed
     */
    public void setMovedAllowed(boolean movAllowed) {
        this.movedAllowed = movAllowed;
    }

    /**
     * Check if movement turn action is currently allowed
     * @return true/false
     */
    public boolean isMovedAllowed() {
        return movedAllowed;
    }
}
