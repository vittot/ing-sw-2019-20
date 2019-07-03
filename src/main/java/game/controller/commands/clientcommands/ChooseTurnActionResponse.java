package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.Action;

/**
 * response to say which action the player selected
 */
public class ChooseTurnActionResponse implements ClientGameMessage {
    /**
     * Action selected by the player
     */
    private Action typeOfAction;

    /**
     * Constructor
     * @param typeOfAction action selected
     */
    public ChooseTurnActionResponse(Action typeOfAction) {
        this.typeOfAction = typeOfAction;
    }

    /**
     *
     * @return the action
     */
    public Action getTypeOfAction() {
        return typeOfAction;
    }

    /**
     * Handle the message
     * @param handler who handle the message
     * @return the message from the handler
     */
     @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */
    @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }
}
