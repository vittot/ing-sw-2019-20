package game.controller.commands.servercommands;

import game.controller.WaitingRoom;
import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

import java.util.List;

/**
 * Response to the client with the list of the waiting room
 */
public class WaitingRoomsListResponse implements ServerGameMessage {
    /**
     * Possible waiting room to join
     */
    private List<WaitingRoom> avaiableWaitingRooms;

    /**
     * Constructor
     * @param avaiableWaitingRooms list of waiting room
     */
    public WaitingRoomsListResponse(List<WaitingRoom> avaiableWaitingRooms) {
        this.avaiableWaitingRooms = avaiableWaitingRooms;
    }

    /**
     *
     * @return the waiting room
     */
    public List<WaitingRoom> getAvaiableWaitingRooms() {
        return avaiableWaitingRooms;
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
