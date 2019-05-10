package game.controller;

import game.controller.commands.ClientMessage;

public interface Client {

    public void sendMessage(ClientMessage msg);

}
