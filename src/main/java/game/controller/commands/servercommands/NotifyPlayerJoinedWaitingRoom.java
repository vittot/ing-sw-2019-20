package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.Player;

/**
 * notify that a player joined the waiting room
 */
public class NotifyPlayerJoinedWaitingRoom implements ServerGameMessage {

    private Player player; /** reference to the player */

    /**
     * construct correct message
     * @param p
     */
    public NotifyPlayerJoinedWaitingRoom(Player p) {
        this.player = p;
    }

    /**
     * return player reference
     * @return player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Handle the message
     * @param handler who handle the messag
     * @param handler
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the messag
     * @param handler
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
