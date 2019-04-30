package game;

import game.controller.GameServer;

import java.io.IOException;

public class LaunchServer {
    public static void main(String[] args) throws IOException {
        GameServer server = new GameServer(5000);

        try {
            server.run();
        } finally {
            server.close();
        }
    }
}
