package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.Target;

import java.util.List;

public class ChooseTargetResponse implements ClientMessage {

    public List<Target> selectedTargets;

    public ChooseTargetResponse(List<Target> selectedTargets) {
        this.selectedTargets = selectedTargets;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) {
        return handler.handle(this);
    }
}
