package game.controller.commands.servercommands;
import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.Action;

import java.util.ArrayList;
import java.util.List;

/**
 * request of single step action choice from the server to the client
 */
public class ChooseSingleActionRequest implements ServerGameMessage {

    private List<Action> actions; /** available actions to choose from */

    /**
     * construct the correct message
     * @param actions
     */
    public ChooseSingleActionRequest(List<Action> actions) {
        this.actions = new ArrayList<>(actions);
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * return the available actions
     * @return actions
     */
    public List<Action> getActions() {
        return actions;
    }
}
