package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;

/**
 * request of log out
 */
public class LogoutRequest implements ClientGameMessage {
    /**
     * player that want to log out
     */
    private String username;

    /**
     * Constructor
     * @param username name
     */
    public LogoutRequest(String username) {
        this.username = username;
    }

    /**
     *
     * @return the name
     */
    public String getUsername() {
        return username;
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
