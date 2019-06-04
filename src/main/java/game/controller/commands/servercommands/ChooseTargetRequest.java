package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Target;
import game.model.effects.SimpleEffect;

import java.util.List;

public class ChooseTargetRequest implements ServerMessage {
    private List<Target> possibleTargets;
    private SimpleEffect currSimpleEffect;

    public ChooseTargetRequest(List<Target> possibleTargets,SimpleEffect currSimpleEffect) {
        this.possibleTargets = possibleTargets;
        this.currSimpleEffect = currSimpleEffect;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

    public List<Target> getPossibleTargets() {
        return possibleTargets;
    }

    public SimpleEffect getCurrSimpleEffect() {
        return currSimpleEffect;
    }
}
