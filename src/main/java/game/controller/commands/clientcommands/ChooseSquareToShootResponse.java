package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.Square;

import java.util.List;

public class ChooseSquareToShootResponse implements ClientGameMessage {

    private List<Square> choosenSquare;
    public ChooseSquareToShootResponse(List<Square> choosenSquare) {
        this.choosenSquare = choosenSquare;
    }

    public List<Square> getChoosenSquare() {
        return choosenSquare;
    }

    @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler){
        return handler.handle(this);
    }

    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
