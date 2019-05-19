package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;

public class CreateWaitingRoomResponse implements ServerMessage {
    private int id;
    public CreateWaitingRoomResponse(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
