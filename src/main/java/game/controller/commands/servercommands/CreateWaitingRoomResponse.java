package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

/**
 * message to notify the occurred creation of a new waiting room
 */
public class CreateWaitingRoomResponse implements ServerGameMessage {
    /**
     * id of the created waiting room
     */
    private int id;

    /**
     * construct correct message
     * @param id
     */
    public CreateWaitingRoomResponse(int id) {
        this.id = id;
    }

    /**
     *
     * @return waiting room id
     */
    public int getId() {
        return id;
    }

    /**
     * Handle the message
     * @param handler who handle the message
     * @return the message from the handler
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the message
     * @return the message from the handler
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
