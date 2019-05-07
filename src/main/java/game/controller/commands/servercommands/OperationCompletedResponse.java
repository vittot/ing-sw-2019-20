package game.controller.commands.servercommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;

public class OperationCompletedResponse implements ServerMessage {
    public ClientMessage operation;

    public OperationCompletedResponse(ClientMessage operation)
    {
        this.operation = operation;
    }

    public OperationCompletedResponse() { }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
