package game.controller.commands;

import game.controller.commands.servercommands.PingMessage;

public interface ServerMessageHandler {
    void handle(ServerGameMessage msg);

    void handle(PingMessage msg);
}
