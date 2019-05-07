package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.model.Target;

import java.util.List;

public class ChooseTargetResponse implements ClientMessage {

    public List<Target> selectedTargets;

    public ChooseTargetResponse(List<Target> selectedTargets) {
        this.selectedTargets = selectedTargets;
    }

    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
