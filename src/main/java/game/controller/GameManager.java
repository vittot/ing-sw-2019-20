
package game.controller;

import game.model.Game;
import game.model.GameMap;
import game.model.Player;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;

/**
 * Menage the different games currently running on the Server
 * It's a singleton
 */
public class GameManager implements Serializable {
    /**
     * Instance of the singleton
     */
    private static GameManager instance;
    /**
     * Id for the next game
     */
    private int nextId;
    /**
     * List of currently created games
     */
    private List<Game> games;
    /**
     * List of existing waiting rooms
     */
    private List<WaitingRoom> waitingRooms;
    /**
     * List of available maps
     */
    private List<GameMap> availableMaps;
    /**
     * List of users logged
     */
    private List<String> usersLogged;
    /**
     * Users suspended associated to their game
     */
    private Map<String,Game> usersSuspended;


    /**
     * Default consructor
     */
    private GameManager(){
        nextId = 1;
        games = new ArrayList<>();
        usersLogged = new ArrayList<>();
        usersSuspended = new HashMap<>();
        availableMaps = XMLParser.getAvailableMaps();
        waitingRooms = new ArrayList<>();
    }

    /**
     * Get the singleton instance
     * @return game manager instance
     */
    public static synchronized GameManager get() {
        if (instance == null) {
            instance = new GameManager();
        }

        return instance;
    }

    /**
     * Return the map with a given id
     * @param mapId map id
     * @return map
     */
    public GameMap getMap(int mapId)
    {
        return availableMaps.stream().filter(m->m.getId() == mapId).findFirst().orElse(null);
    }

    /**
     * Return the list of available maps
     * @return maps list
     */
    public List<GameMap> getAvailableMaps() {
        return availableMaps;
    }

    /**
     * Add a new logged user
     * @param user username
     */
    public void addLoggedUser(String user)
    {
        usersLogged.add(user);
    }

    /**
     * Suspend a player
     * @param player player to be suspended
     */
    public void suspendPlayer(Player player)
    {
        if(usersLogged.contains(player.getNickName()))
        {
            usersLogged.remove(player.getNickName());
            usersSuspended.put(player.getNickName(),player.getGame());
        }
    }

    /**
     * Remove all users suspended from an ended game
     * @param g game
     */
    public void endGame(Game g)
    {
        games.remove(g);
        List<String> usersSuspendedToBeRemove = new ArrayList<>();
        for(Map.Entry<String, Game> e : usersSuspended.entrySet())
            if(e.getValue() == g)
                usersSuspendedToBeRemove.add(e.getKey());
        for(String s : usersSuspendedToBeRemove)
            usersSuspended.remove(s);
    }

    /**
     * Rejoin a user previously suspended to its old game
     * @param user username
     */
    void rejoinUser(String user)
    {
        Game g = usersSuspended.remove(user);
        if(g == null)
            g=getGameOfUser(user);
        g.rejoinUser(user);
        usersLogged.add(user);
    }

    /**
     * Return logged users list
     * @return usernames list
     */
    List<String> getUsersLogged() {
        return usersLogged;
    }

    /**
     * Return suspend users list
     * @return usernames list
     */
    Set<String> getUsersSuspended() {
        return usersSuspended.keySet();
    }

    /**
     * Create a new Game
     * @param mapId map id for the game
     * @param players players for the game
     * @return the new game
     */
    Game addGame(int mapId, List<Player> players){
        Game g = new Game(nextId, mapId, 8,players);
        nextId++;
        games.add(g);
        return g;
    }

    /**
     * Return all available waitingRooms
     * @return waiting rooms list
     */
    public List<WaitingRoom> getWaitingRooms() {
        return waitingRooms;
    }

    /**
     * Remove a waiting room
     * @param waitingRoom waiting room to be removed
     */
    void removeWaitingRoom(WaitingRoom waitingRoom) {
        this.waitingRooms.remove(waitingRoom);
    }

    /**
     * Get the waiting room with the indicated id
     * @param roomId id
     * @return the waiting room or null if it does not exist
     */
    public WaitingRoom getWaitingRoom(int roomId) {
        return waitingRooms.stream().filter(w -> w.getId() == roomId).findFirst().orElse(null);
    }

    /**
     * Add a new WaitingRoom with the given mapId
     * @param mapId map id
     * @return the WaitingRoom created
     */
    WaitingRoom addWaitingRoom(int mapId) {
        waitingRooms.add(new WaitingRoom(waitingRooms.size()+1,mapId));
        return waitingRooms.get(waitingRooms.size()-1);
    }

    /**
     * Return the game in which was involved a suspender user
     * @param nickname username
     * @return the game (or null)
     */
    Game getGameOfSuspendedUser(String nickname) {
        return usersSuspended.get(nickname);
    }

    /**
     * Return the game in which is playing a certain user
     * @param nickname username
     * @return game (or null)
     */
    Game getGameOfUser(String nickname)
    {
        for(Game g : games)
        {
            if(g.getPlayers().stream().filter(p->p.getNickName().equals(nickname)).count() > 0)
                return g;
        }
        return null;
    }

    /**
     * Remove a user previously logged
     * @param nickname username
     */
    public void removeLoggedUser(String nickname)
    {
        this.usersLogged.remove(nickname);
    }
}
