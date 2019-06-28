package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.Square;

public class ChooseSquareResponse implements ClientGameMessage {


    private Square selectedSquare;

    public ChooseSquareResponse(Square selectedSquare) {
        this.selectedSquare=selectedSquare;
    }

    public Square getSelectedSquare() {
        return selectedSquare;
    }

    @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }

    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}