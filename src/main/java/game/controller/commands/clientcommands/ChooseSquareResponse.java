package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.Square;
import game.model.exceptions.MapOutOfLimitException;

import java.util.List;

public class ChooseSquareResponse implements ClientMessage {


    private Square selectedSquare;

    public ChooseSquareResponse(Square selectedSquare) {
        this.selectedSquare=selectedSquare;
    }

    public Square getSelectedSquare() {
        return selectedSquare;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) throws MapOutOfLimitException {
        return handler.handle(this);
    }
}