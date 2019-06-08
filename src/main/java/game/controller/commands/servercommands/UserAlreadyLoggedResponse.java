package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.exceptions.MapOutOfLimitException;

public class UserAlreadyLoggedResponse implements ServerMessage {
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
