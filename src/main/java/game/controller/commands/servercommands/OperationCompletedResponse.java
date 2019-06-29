package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

public class OperationCompletedResponse implements ServerGameMessage {
    private String message;

    public OperationCompletedResponse() { }

    public OperationCompletedResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
