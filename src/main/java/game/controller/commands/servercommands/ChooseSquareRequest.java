package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.Square;

import java.util.List;

public class ChooseSquareRequest implements ServerGameMessage {

    private List<Square> possiblePositions;

    public ChooseSquareRequest(List<Square> possiblePositions) {
        this.possiblePositions = possiblePositions;
    }

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

    public List<Square> getPossiblePositions() {
        return possiblePositions;
    }
}
