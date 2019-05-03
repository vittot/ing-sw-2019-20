package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;

public class GrabActionRequest implements ClientMessage {
    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
