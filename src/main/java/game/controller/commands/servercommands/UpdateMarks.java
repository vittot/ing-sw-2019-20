package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.Player;

/**
 * Update marks value from this turn marks to real marks in the client context
 */
public class UpdateMarks implements ServerGameMessage {
    /**
     * Id of the player
     */
    private int id;

    /**
     * Constructor
     * @param id int player
     */
    public UpdateMarks(int id) {
        this.id = id;
    }

    /**
     *
     * @return the id
     */
    public int getId() {
        return id;
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
