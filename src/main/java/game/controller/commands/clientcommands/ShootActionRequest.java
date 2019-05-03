package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;

public class ShootActionRequest implements ClientMessage {
    //Clint sent send weapon and order list.effect
    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
