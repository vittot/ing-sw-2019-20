package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.Square;

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
    public ServerMessage handle(ClientMessageHandler handler) {
        return handler.handle(this);
    }
}