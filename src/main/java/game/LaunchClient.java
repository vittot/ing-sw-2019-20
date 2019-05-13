package game;

import game.controller.Client;
import game.controller.ClientController;
import game.controller.SocketClient;

import java.io.IOException;

public class LaunchClient {
    public static void main(String[] args) throws IOException {
        Client client = new SocketClient("127.0.0.1",5000);
        client.init();
        ClientController controller = new ClientController(client);
        controller.run();

        client.close();
    }
}
