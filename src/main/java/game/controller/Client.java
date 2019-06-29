package game.controller;

import game.controller.commands.ClientMessage;

public interface Client {

    void sendMessage(ClientMessage msg);
    void startListening(ClientController handler);
    boolean init();

    //TODO: check if this make sense also in RMI:
    public void close();
}
