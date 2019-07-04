package game.controller.commands.clientcommands;

import game.controller.commands.*;

/**
 * Request of reload action
 */
public class ReloadWeaponAction implements ClientGameMessage {

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
