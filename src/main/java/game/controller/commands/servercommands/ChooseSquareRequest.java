package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.Square;

import java.util.List;

/**
 * request of choice a square to complete the movement action
 */
public class ChooseSquareRequest implements ServerGameMessage {

    private List<Square> possiblePositions; /** possible square to choose from */

    /**
     * construct the correct message
     * @param possiblePositions
     */
    public ChooseSquareRequest(List<Square> possiblePositions) {
        this.possiblePositions = possiblePositions;
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

    /**
     * return the possiblePositions
     * @return possiblePositions
     */
    public List<Square> getPossiblePositions() {
        return possiblePositions;
    }
}
