package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.Square;

/**
 * response to say which square the client selected
 */
public class ChooseSquareResponse implements ClientGameMessage {

    /**
     * selected square
     */
    private Square selectedSquare;

    /**
     * Constructor
     * @param selectedSquare square selected
     */
    public ChooseSquareResponse(Square selectedSquare) {
        this.selectedSquare=selectedSquare;
    }

    /**
     *
     * @return the square selected
     */
    public Square getSelectedSquare() {
        return selectedSquare;
    }

    /**
     * Handle the message
     * @param handler who handle the message
     * @return the message from the handler
     */
    @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */
    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}