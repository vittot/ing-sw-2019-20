package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.Target;
import game.model.effects.SimpleEffect;

import java.util.List;

/**
 * message to allow the choice of possible targets to choose from to apply the effect selected
 */
public class ChooseTargetRequest implements ServerGameMessage {

    /**
     * list of possible targets to choose from
     */
    private List<Target> possibleTargets;
    /**
     * effect selected
     */
    private SimpleEffect currSimpleEffect;

    /**
     * construct the correct message
     * @param possibleTargets
     * @param currSimpleEffect
     */
    public ChooseTargetRequest(List<Target> possibleTargets,SimpleEffect currSimpleEffect) {
        this.possibleTargets = possibleTargets;
        this.currSimpleEffect = currSimpleEffect;
    }

    /**
     * Handle the message
     * @param handler who handle the message
     * @return the message from the handler
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the message
     * @return the message from the handler
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * return the possible targets
     * @return possibleTargets
     */
    public List<Target> getPossibleTargets() {
        return possibleTargets;
    }

    /**
     * return the effect used
     * @return currSimpleEffect
     */
    public SimpleEffect getCurrSimpleEffect() {
        return currSimpleEffect;
    }
}
