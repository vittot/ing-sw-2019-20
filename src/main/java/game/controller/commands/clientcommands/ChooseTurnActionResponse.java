package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.Action;

public class ChooseTurnActionResponse implements ClientGameMessage {
    private Action typeOfAction;

    public ChooseTurnActionResponse(Action typeOfAction) {
        this.typeOfAction = typeOfAction;
    }

    public Action getTypeOfAction() {
        return typeOfAction;
    }

     @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }
}
