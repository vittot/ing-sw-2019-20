package game.controller;

import game.controller.commands.ClientMessage;
import game.controller.commands.ServerMessage;

public class RMIClient implements Client {

    @Override
    public void sendMessage(ClientMessage msg) {

    }

    @Override
    public ServerMessage receiveMessage() {
        return null;
    }

    @Override
    public void init() {

    }

    @Override
    public void close() {

    }
}
