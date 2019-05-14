package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Target;

import java.util.List;

public class ChooseTargetRequest implements ServerMessage {
    private List<Target> possibleTargets;

    public ChooseTargetRequest(List<Target> possibleTargets) {
        this.possibleTargets = possibleTargets;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

    public List<Target> getPossibleTargets() {
        return possibleTargets;
    }
}
