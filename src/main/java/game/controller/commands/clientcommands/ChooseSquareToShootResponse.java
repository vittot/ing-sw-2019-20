package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.Square;

import java.util.List;

/**
 * Response to say which square the player selected to shoot
 */
public class ChooseSquareToShootResponse implements ClientGameMessage {
    /**
     * List of square selected
     */
    private List<Square> choosenSquare;

    /**
     * Constructor
     * @param choosenSquare
     */
    public ChooseSquareToShootResponse(List<Square> choosenSquare) {
        this.choosenSquare = choosenSquare;
    }

    /**
     *
     * @return the list of square
     */
    public List<Square> getChoosenSquare() {
        return choosenSquare;
    }

    /**
     * Handle the message
     * @param handler who handle the message
     * @return the message from the handler
     */
    @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler){
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
