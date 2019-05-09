package game.controller;


import game.model.Game;
import game.model.Player;
import game.model.PlayerColor;

import java.util.*;

public class WaitingRoom {
    private int id;
    private Map<ServerController,Player> players;
    private int numWaitingPlayers;
    private int mapId;

    public WaitingRoom(int id, int mapId, int numWaitingPlayers)
    {
        this.mapId = mapId;
        this.id = id;
        players = new HashMap<>();
        this.numWaitingPlayers = numWaitingPlayers;
    }

    public Collection<Player> getPlayers()
    {
        return players.values();
    }


    public int getId() {
        return this.id;
    }

    /**
     * Add a waiting client in this room, when the room is full it starts the game and notify the clients about it
     * @param s
     */
    public void addWaitingPlayer(ServerController s)
    {
        int n = players.size();
        Player p = new Player(n+1, PlayerColor.values()[n]);
        players.put(s,p);
        if(players.size() == numWaitingPlayers){
            Game g = GameManager.get().addGame(mapId);
            g.setPlayers(new ArrayList<>(players.values()));
            GameManager.get().removeWaitingRoom(this);
            for(Map.Entry<ServerController, Player> e: players.entrySet())
            {
                e.getKey().startGame(g, e.getValue());
            }
        }

    }

}
