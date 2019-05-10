package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.Action;

public class ChooseTurnActionResponse implements ClientMessage {
    public Action typeOfAction;

    public ChooseTurnActionResponse(Action typeOfAction) {
        this.typeOfAction = typeOfAction;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) {
        return handler.handle(this);
    }
}
