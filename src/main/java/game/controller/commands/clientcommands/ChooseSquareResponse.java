package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;

public class ChooseSquareResponse implements ClientMessage {


    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}