package game;

import game.controller.Client;
import game.controller.ClientController;
import game.controller.RMIClient;
import game.controller.SocketClient;

import java.io.IOException;

public class LaunchClient {
    public static void main(String[] args) throws IOException {
        String connection = args[0];
        Client client;
        if(connection.equals("RMI"))
            client = new RMIClient();
        else
            client = new SocketClient("localhost",5000);
        client.init();
        ClientController controller = new ClientController(client,args);
        controller.run();

    }
}
