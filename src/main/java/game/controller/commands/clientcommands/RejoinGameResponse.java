package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;

/**
 * Request of rejoin the game after a time out
 */
public class RejoinGameResponse implements ClientGameMessage {
    /**
     * true if the player want to rejoin
     */
    private boolean rejoin;
    /**
     * user name that got timed out
     */
    private String user;

    /**
     * Constructor
     * @param rejoin flag
     * @param user name
     */
    public RejoinGameResponse(boolean rejoin,String user) {
        this.rejoin = rejoin;
        this.user = user;
    }

    /**
     *
     * @return true if the player want to rejoin
     */
    public boolean isRejoin() {
        return rejoin;
    }

    /**
     *
     * @return username
     */
    public String getUser() {
        return user;
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
