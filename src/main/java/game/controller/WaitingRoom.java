package game.controller;


import game.model.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Class that identify the waiting room
 */
public class WaitingRoom implements Serializable {
    /**
     * Identifier of the waiting room
     */
    private int id;
    /**
     * Map of the player and the his Server Controller reference
     */
    private transient Map<ServerController,Player> players;
    /**
     * Number of player in the waiting room
     */
    private int numWaitingPlayers;
    /**
     * Id of the map selected for the waiting room
     */
    private int mapId;
    /**
     * Minimun player to start a game
     */
    private final int N_MINIMUM_PLAYERS = 3;
    /**
     * Number of mac player
     */
    private final int NUM_PLAYERS = 5;
    /**
     * Timer before the game start after reaching the minimun number of player
     */
    private transient ScheduledExecutorService timer;

    /**
     * Constructor
     * @param id id
     * @param mapId map id
     */
    public WaitingRoom(int id, int mapId)
    {
        this.mapId = mapId;
        this.id = id;
        players = new HashMap<>();
        this.numWaitingPlayers = NUM_PLAYERS;
    }

    /**
     *
     * @return the number of player in waiting
     */
    int getNumWaitingPlayers() {
        return numWaitingPlayers;
    }

    /**
     *
     * @return the player in waiting
     */
    public Collection<Player> getPlayers()
    {
        return players.values();
    }

    /**
     *
     * @return the waiting room id
     */
    public int getId() {
        return this.id;
    }

    /**
     *
     * @return the collection of all server controller from the other player
     */
    public Collection<ServerController> getServerControllers()
    {
        return players.keySet();
    }

    /**
     * Add a waiting client in this room, when the room is full it starts the game and notify the clients about it
     * @param s
     */
    int addWaitingPlayer(ServerController s, String nick)
    {
        int n = players.size();
        Player p = new Player(n+1, PlayerColor.values()[n],nick);
        p.setPlayerObserver(s);
        notifyPlayerJoin(p);
        players.put(s,p);
        s.setWaitingRoom(this);
        if(players.size() == numWaitingPlayers){
            startGame();
        }
        else if(players.size() == N_MINIMUM_PLAYERS)
        {
            startTimer();
        }
        return n+1;

    }

    /**
     * Start the waiting room timer at the end of which the game will be started
     */
    private void startTimer()
    {
        timer = Executors.newSingleThreadScheduledExecutor();
        Runnable r = this::startGame;
        timer.schedule(r,Configuration.WAITING_ROOM_TIMER_MS, TimeUnit.MILLISECONDS);
    }

    /**
     * Reset the waiting room timer
     */
    private void stopTimer()
    {
        timer.shutdownNow();
    }

    /**
     * Start the game
     */
    private void startGame()
    {
        Game g = GameManager.get().addGame(mapId,new ArrayList<>(players.values()));
        GameManager.get().removeWaitingRoom(this);
        g.refillMap();
        for(Map.Entry<ServerController, Player> e: players.entrySet())
        {
            e.getKey().startGame(g, e.getValue());
            g.addGameListener(e.getKey().getClientHandler());
        }
        players.entrySet().iterator().next().getKey().launchGame(this);


    }

    /**
     * Notify all players that a new Player joined the waiting room
     * @param p
     */
    private void notifyPlayerJoin(Player p) {
       players.keySet().forEach(sc -> sc.notifyPlayerJoinedWaitingRoom(p));
    }

    /**
     * Remove a player from the waitingroom
     * @param sc ServerController of the player which need to be removed
     */
    void removeWaitingPlayer(ServerController sc)
    {
        Player p = players.remove(sc);
        GameManager.get().getUsersLogged().remove(p.getNickName());
        if(players.size() < N_MINIMUM_PLAYERS)
            stopTimer();
        for(ServerController s: players.keySet())
        {
            s.notifyPlayerExitedFromWaitingRoom(p.getId());
        }


    }


    /**
     * Serialize the object, excluding the player controllers
     * @param oos outputs stream
     * @throws IOException  in case of communication error
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        //Serialize only the players, without their controllers
        /*for(Player p : players.values())
            oos.writeObject(p);*/
        List<Player> serPlayer = new ArrayList<>();
        for(Player p : players.values())
            serPlayer.add(p);
        oos.writeObject((Serializable)serPlayer);
    }

    /**
     * De-serialize the object. The players are associated to null controllers
     * @param ois input stream
     * @throws IOException  in case of communication error
     */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        List<Player> recPlayer = (ArrayList<Player>)ois.readObject();
        players = new HashMap<>();
        for(Player p : recPlayer)
            players.put(new ServerController(), p);
    }

    /**
     * Return a string representation of the waiting room
     * @return string
     */
    @Override
    public String toString()
    {
        StringBuilder rep = new StringBuilder("Waiting Room n. " + id + "\nMap id: " + mapId + "\nNumber of players to be reached: " + numWaitingPlayers + "\nPlayer:\n");
        for(Player p : getPlayers())
            rep.append(p.toShortString() + "\n");
        return rep.toString();
    }

    /**
     * Return true if the waiting room is empty, false otherwise
     * @return boolean
     */
    public boolean isEmpty() {
        return players.isEmpty();
    }
}
