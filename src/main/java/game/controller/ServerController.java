package game.controller;

import game.model.Game;

public class ServerController {
    // reference to the Networking layer
    private final SocketClientHandler clientHandler;

    // reference to the Model
    private Game model;

    public ServerController(SocketClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    /**
     * Create a new Game for this client
     */
    public void setNewGame()
    {

    }

    /**
     * Join the client to an existing Game
     * @param gameId
     */
    public void joinGame(int gameId){

    }

    // TODO: ------ Request handling
}
