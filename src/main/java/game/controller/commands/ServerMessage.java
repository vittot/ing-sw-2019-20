package game.controller.commands;

import java.io.Serializable;

public interface ServerMessage extends Serializable {
    void handle(ServerMessageHandler handler) ;
}
