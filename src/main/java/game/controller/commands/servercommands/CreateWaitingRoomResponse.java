package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

public class CreateWaitingRoomResponse implements ServerGameMessage {
    private int id;
    public CreateWaitingRoomResponse(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
