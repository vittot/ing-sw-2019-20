package game.controller.commands.servercommands;

import game.controller.WaitingRoom;
import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

import java.util.List;

public class WaitingRoomsListResponse implements ServerGameMessage {

    private List<WaitingRoom> avaiableWaitingRooms;

    public WaitingRoomsListResponse(List<WaitingRoom> avaiableWaitingRooms) {
        this.avaiableWaitingRooms = avaiableWaitingRooms;
    }

    public List<WaitingRoom> getAvaiableWaitingRooms() {
        return avaiableWaitingRooms;
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
