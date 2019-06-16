package game.controller;

import game.controller.commands.ServerMessage;
import game.model.*;
import game.model.exceptions.InsufficientAmmoException;


public interface ClientHandler extends GameListener {
    void sendMessage(ServerMessage msg);
}
