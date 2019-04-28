package game.controller;

import game.model.Game;

import java.util.ArrayList;
import java.util.List;

/**
 * Menage the different games currently running on the Server
 * It's a singleton
 */
public class GameManager {
    private static GameManager instance;

    private static List<Game> games;

    private GameManager(){
        games = new ArrayList<>();
    }

    public static synchronized GameManager get() {
        if (instance == null) {
            instance = new GameManager();
        }

        return instance;
    }

}
