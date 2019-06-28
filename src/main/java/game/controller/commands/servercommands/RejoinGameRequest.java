package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

import java.util.List;

public class RejoinGameRequest implements ServerGameMessage {

    private List<String> otherPlayerNames;

    public RejoinGameRequest(List<String> otherPlayerNames) {
        this.otherPlayerNames = otherPlayerNames;
    }

    public List<String> getOtherPlayerNames() {
        return otherPlayerNames;
    }

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
