package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.Target;

import java.util.List;

public class ChooseTargetResponse implements ClientGameMessage {

    private List<Target> selectedTargets;

    public ChooseTargetResponse(List<Target> selectedTargets) {
        this.selectedTargets = selectedTargets;
    }

    public List<Target> getSelectedTargets() {
        return selectedTargets;
    }

     @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }

    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
