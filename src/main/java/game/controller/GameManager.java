package game.controller;

import game.model.Game;
import game.model.GameMap;
import game.model.Player;

import java.util.*;

/**
 * Menage the different games currently running on the Server
 * It's a singleton
 */
public class GameManager {
    private static GameManager instance;
    private int nextId;
    private List<Game> games;
    private List<WaitingRoom> waitingRooms;
    private List<GameMap> availableMaps;
    private List<String> usersLogged;
    private Map<String,Game> usersSuspended;


    private GameManager(){
        nextId = 1;
        games = new ArrayList<>();
        usersLogged = new ArrayList<>();
        usersSuspended = new HashMap<>();
        availableMaps = Game.getAvailableMaps();
        waitingRooms = new ArrayList<>();
    }

    public static synchronized GameManager get() {
        if (instance == null) {
            instance = new GameManager();
        }

        return instance;
    }

    public GameMap getMap(int mapId)
    {
        return availableMaps.get(mapId);
    }

    public void addLoggedUser(String user)
    {
        usersLogged.add(user);
    }

    public void suspendPlayer(Player player)
    {
        usersLogged.remove(player.getNickName());
        usersSuspended.put(player.getNickName(),player.getGame());
    }

    /**
     * Rejoin a user previously suspended to its old game
     * @param user
     */
    public void rejoinUser(String user)
    {
        Game g = usersSuspended.remove(user);
        g.rejoinUser(user);
        usersLogged.add(user);
    }

    public List<String> getUsersLogged() {
        return usersLogged;
    }

    public Set<String> getUsersSuspended() {
        return usersSuspended.keySet();
    }

    /**
     * Add a new Game to t
     * @param mapId
     * @return
     */
    public Game addGame(int mapId, List<Player> players){
        Game g = new Game(nextId, mapId, 8,players);
        g.setMap(getMap(mapId));
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
     * @return the WaitingRoom created
     */
    public WaitingRoom addWaitingRoom(int mapId, int numWaitingPlayers) {
        waitingRooms.add(new WaitingRoom(waitingRooms.size()+1,mapId, numWaitingPlayers));
        return waitingRooms.get(waitingRooms.size()-1);
    }

    /**
     * Return the game in which was involveed a suspender user
     * @param nickname
     * @return
     */
    public Game getNameOfSuspendedUser(String nickname) {
        return usersSuspended.get(nickname);
    }

    /**
     * Remove a user previously logged
     * @param nickname username
     */
    void removeLoggedUser(String nickname)
    {
        this.usersLogged.remove(nickname);
    }
}
