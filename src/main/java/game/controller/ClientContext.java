package game.controller;

import game.model.Kill;
import java.util.List;
import game.model.GameMap;
import game.model.effects.SimpleEffect;

/**
 * Model portion for the Client
 * It's a singleton for every Client
 */
public class ClientContext {
    private static ClientContext instance;

    private GameMap map;
    private int myID;
    private List<Kill> killboard;
    private SimpleEffect currentEffect;

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
