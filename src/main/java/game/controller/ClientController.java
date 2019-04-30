package game.controller;

import java.io.IOException;

public class ClientController {
    // reference to networking layer
    private final Client client;
    private Thread receiver;

    //TODO: add the view

    public ClientController(Client client) {
        this.client = client;
    }


    public void start() {

        receiver = new Thread(
                //receive response from the Server
        );
        receiver.start();
    }


    public void run() throws IOException {
        //TODO: launch various phases on the view
    }
}
