package game.controller;

import game.controller.commands.ClientMessage;
import game.controller.commands.ServerMessage;

public interface Client {

    public void sendMessage(ClientMessage msg);
    ServerMessage receiveMessage();

    //TODO: check if these make sense also in RMI:
    public void init();
    public void close();

}
