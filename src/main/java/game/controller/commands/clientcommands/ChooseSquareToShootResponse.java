package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.Square;
import game.model.Target;
import game.model.exceptions.InsufficientAmmoException;
import game.model.exceptions.MapOutOfLimitException;
import game.model.exceptions.NoCardAmmoAvailableException;

import java.util.List;

public class ChooseSquareToShootResponse implements ClientMessage {

    private List<Square> choosenSquare;
    public ChooseSquareToShootResponse(List<Square> choosenSquare) {
        this.choosenSquare = choosenSquare;
    }

    public List<Square> getChoosenSquare() {
        return choosenSquare;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler){
        return handler.handle(this);
    }
}
