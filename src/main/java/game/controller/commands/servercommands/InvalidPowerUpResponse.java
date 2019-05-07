package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;

public class InvalidPowerUpResponse implements ServerMessage {
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}