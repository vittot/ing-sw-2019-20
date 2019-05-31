package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.exceptions.MapOutOfLimitException;

import java.util.List;

public class RejoinGameRequest implements ServerMessage {

    private List<String> otherPlayerNames;

    public RejoinGameRequest(List<String> otherPlayerNames) {
        this.otherPlayerNames = otherPlayerNames;
    }

    public List<String> getOtherPlayerNames() {
        return otherPlayerNames;
    }

    @Override
    public void handle(ServerMessageHandler handler) throws MapOutOfLimitException {
        handler.handle(this);
    }
}
