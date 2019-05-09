package game.controller.commands.servercommands;

import game.controller.WaitingRoom;
import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;

import java.util.List;

public class WaitingRoomsListResponse implements ServerMessage {

    private List<WaitingRoom> avaiableWaitingRooms;

    public WaitingRoomsListResponse(List<WaitingRoom> avaiableWaitingRooms) {
        this.avaiableWaitingRooms = avaiableWaitingRooms;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
