package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.Target;

import java.util.List;

/**
 * Response to say which target the player selected
 */
public class ChooseTargetResponse implements ClientGameMessage {
    /**
     * list of target selected
     */
    private List<Target> selectedTargets;

    /**
     * Construnctor
     * @param selectedTargets
     */
    public ChooseTargetResponse(List<Target> selectedTargets) {
        this.selectedTargets = selectedTargets;
    }

    /**
     *
     * @return return the target selected
     */
    public List<Target> getSelectedTargets() {
        return selectedTargets;
    }

    /**
     * Handle the message
     * @param handler who handle the message
     * @return the message from the handler
     */
     @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */
    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
