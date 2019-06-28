package game.controller.commands;

import java.io.Serializable;

public interface ClientMessage extends Serializable {
    void handle(ClientMessageHandler handler) ;
}
