package game.controller;

import game.model.Game;
import game.model.Map;

import java.util.ArrayList;
import java.util.List;

/**
 * Menage the different games currently running on the Server
 * It's a singleton
 */
public class GameManager {
    private static GameManager instance;
    private int nextId;
    private List<Game> games;
    private List<WaitingRoom> waitingRooms;
    private List<Map> availableMaps;

    private GameManager(){
        nextId = 1;
        games = new ArrayList<>();
        availableMaps = new ArrayList<>();
    }

    public static synchronized GameManager get() {
        if (instance == null) {
            instance = new GameManager();
        }

        return instance;
    }

    public Map getMap(int mapId)
    {
        return availableMaps.get(mapId);
    }

    /**
     * Add a new Game to t
     * @param mapId
     * @return
     */
    public Game addGame(int mapId){
        Game g = new Game(nextId, mapId, 8);
        nextId++;
        games.add(g);
        return g;
    }

    /**
     * Return all avaiable waitingRooms
     * @return
     */
    public List<WaitingRoom> getWaitingRooms() {
        return waitingRooms;
    }

    /**
     * Remove a full waiting room (it will become a Game)
     * @param waitingRoom
     */
    public void removeWaitingRoom(WaitingRoom waitingRoom) {
        this.waitingRooms.remove(waitingRoom);
    }

    /**
     * Get the waiting room with the indicated id
     * @param roomId
     * @return the waiting room or null if it does not exist
     */
    public WaitingRoom getWaitingRoom(int roomId) {
        return waitingRooms.stream().filter(w -> w.getId() == roomId).findFirst().orElse(null);
    }

    /**
     * Add a new WaitingRoom with the given mapId and num of players
     * @param mapId
     * @param numWaitingPlayers
     */
    public void addWaitingRoom(int mapId, int numWaitingPlayers) {
        waitingRooms.add(new WaitingRoom(waitingRooms.size()+1,mapId, numWaitingPlayers));
    }
}
