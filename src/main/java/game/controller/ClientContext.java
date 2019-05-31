package game.controller;

import game.model.Kill;
import java.util.List;
import game.model.GameMap;
import game.model.Player;
import game.model.effects.SimpleEffect;

/**
 * Model portion for the Client
 * It's a singleton for every Client
 */
public class ClientContext {
    private static ClientContext instance;

    private GameMap map;
    private int myID;
    private String user;
    private List<Kill> killboard;
    private SimpleEffect currentEffect;
    private List<Player> playersInWaiting;

    private ClientContext() {
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Player getMyPlayer(){
        return map.getPlayerById(myID);
    }

    public List<Player> getPlayersInWaiting() {
        return playersInWaiting;
    }

    public void setPlayersInWaiting(List<Player> playersInWaiting) {
        this.playersInWaiting = playersInWaiting;
    }

    public SimpleEffect getCurrentEffect() {
        return currentEffect;
    }

    public void setCurrentEffect(SimpleEffect currentEffect) {
        this.currentEffect = currentEffect;
    }

    public GameMap getMap() {
        return map;
    }

    public void setMap(GameMap map) {
        this.map = map;
    }

    public static ClientContext getInstance() {
        return instance;
    }

    public static void setInstance(ClientContext instance) {
        ClientContext.instance = instance;
    }

    public int getMyID() {
        return myID;
    }

    public void setMyID(int myID) {
        this.myID = myID;
    }

    public List<Kill> getKillboard() {
        return killboard;
    }

    public void setKillboard(List<Kill> killboard) {
        this.killboard = killboard;
    }
}
