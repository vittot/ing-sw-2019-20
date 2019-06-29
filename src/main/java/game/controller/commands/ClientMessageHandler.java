package game.controller.commands;

import game.controller.commands.clientcommands.PongMessage;

public interface ClientMessageHandler {
    void handle(ClientGameMessage msg);

    void handle(PongMessage msg);
}
