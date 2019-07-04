package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

import java.util.List;

/**
 * Ask if the player want to rejoin nthe game after a time out
 */
public class RejoinGameRequest implements ServerGameMessage {
    /**
     * Othe player in the game
     */
    private List<String> otherPlayerNames;

    /**
     * Constructor
     * @param otherPlayerNames list of player
     */
    public RejoinGameRequest(List<String> otherPlayerNames) {
        this.otherPlayerNames = otherPlayerNames;
    }

    /**
     *
     * @return the player in the game
     */
    public List<String> getOtherPlayerNames() {
        return otherPlayerNames;
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
}
