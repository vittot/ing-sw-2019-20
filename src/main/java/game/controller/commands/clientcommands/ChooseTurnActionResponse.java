package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.model.Action;

public class ChooseTurnActionResponse implements ClientMessage {
    public Action typeOfAction;

    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
