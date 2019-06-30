package game.controller.commands;

import game.controller.commands.clientcommands.LoginMessage;
import game.controller.commands.clientcommands.PongMessage;

public interface ClientMessageHandler {
    void handle(ClientGameMessage msg);

    void handle(PongMessage msg);

    void handle(LoginMessage login);
}
