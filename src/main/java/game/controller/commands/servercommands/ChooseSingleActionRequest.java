package game.controller.commands.servercommands;
import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.Action;

import java.util.ArrayList;
import java.util.List;

public class ChooseSingleActionRequest implements ServerGameMessage {
    private List<Action> actions = new ArrayList<>();

    public ChooseSingleActionRequest(List<Action> actions) {
        this.actions = new ArrayList<>(actions);
    }

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

    public List<Action> getActions() {
        return actions;
    }
}
