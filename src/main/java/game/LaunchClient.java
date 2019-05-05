package game;

import game.controller.Client;
import game.controller.ClientController;

import java.io.IOException;

public class LaunchClient {
    public static void main(String[] args) throws IOException {
        Client client = null;
        //client.init();
        ClientController controller = new ClientController(client);
        //controller.run();

        //client.close();
    }
}
