package game.controller;

import game.controller.commands.ServerMessage;
import game.model.*;


public interface ClientHandler extends GameListener {
    void sendMessage(ServerMessage msg);
}
