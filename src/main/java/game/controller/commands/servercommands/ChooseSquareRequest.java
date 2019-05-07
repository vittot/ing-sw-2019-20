package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Square;

import java.util.List;

public class ChooseSquareRequest implements ServerMessage {

    public List<Square> possiblePositions;

    public ChooseSquareRequest(List<Square> possiblePositions) {
        this.possiblePositions = possiblePositions;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
