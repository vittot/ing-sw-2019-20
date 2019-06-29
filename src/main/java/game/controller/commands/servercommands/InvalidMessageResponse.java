package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

public class InvalidMessageResponse implements ServerGameMessage {

    private String message;

    public InvalidMessageResponse(String message) {
        this.message = message;
    }

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

    public String getMessage() {
        return message;
    }
}
