package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.Player;

/**
 * Ask to the server if the weapons selected is valid
 */
public class CheckValidWeaponRequest implements ClientGameMessage {
    /**
     * Player in current turn
     */
    private Player player;

    /**
     * Constructor
     * @param player player in turn
     */
    public CheckValidWeaponRequest(Player player) {
        this.player = player;
    }

    /**
     * get the player
     * @return player
     */
    public Player getPlayer() {
        return player;
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
