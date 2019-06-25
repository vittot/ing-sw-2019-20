package game.controller;

import game.controller.commands.ClientMessage;
import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;

public interface Client {

    void sendMessage(ClientMessage msg);
    void startListening(ClientController handler);
    void init();

    //TODO: check if this make sense also in RMI:
    public void close();
}
