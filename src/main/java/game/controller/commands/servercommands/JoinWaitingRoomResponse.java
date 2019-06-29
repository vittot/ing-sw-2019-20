package game.controller.commands.servercommands;

import game.controller.WaitingRoom;
import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

public class JoinWaitingRoomResponse implements ServerGameMessage {
    private int id;
    private WaitingRoom waitingRoom;


    public JoinWaitingRoomResponse(int id, WaitingRoom waitingRoom) {
        this.id = id;
        this.waitingRoom = waitingRoom;
    }

    public int getId() {
        return id;
    }

    public WaitingRoom getWaitingRoom() {
        return waitingRoom;
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
