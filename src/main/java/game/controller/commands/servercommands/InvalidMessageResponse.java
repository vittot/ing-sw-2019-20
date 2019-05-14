package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;

public class InvalidMessageResponse implements ServerMessage {

    private String message;

    public InvalidMessageResponse(String message) {
        this.message = message;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

    public String getMessage() {
        return message;
    }
}
