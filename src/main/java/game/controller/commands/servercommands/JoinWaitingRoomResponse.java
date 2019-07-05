package game.controller.commands.servercommands;

import game.controller.WaitingRoom;
import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

/**
 * message that confirm the occurred join into the selected waiting room
 */
public class JoinWaitingRoomResponse implements ServerGameMessage {

    /**
     * id of the player in the waiting room joined
     */
    private int id;

    /**
     * waiting room joined
     */
    private WaitingRoom waitingRoom;

    /**
     * construct correct message
     * @param id
     * @param waitingRoom
     */
    public JoinWaitingRoomResponse(int id, WaitingRoom waitingRoom) {
        this.id = id;
        this.waitingRoom = waitingRoom;
    }

    /**
     * return id attribute
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * return waiting room reference
     * @return waitingRoom
     */
    public WaitingRoom getWaitingRoom() {
        return waitingRoom;
    }

    /**
     * Handle the message
     * @param handler who handle the messag
     * @param handler
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the messag
     * @param handler
    */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
