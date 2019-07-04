package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;

/**
 * Message of login from client to the server
 */
public class LoginMessage implements ClientGameMessage {
    /**
     * Nickname of the player
     */
    private String nickname;
    /**
     * Boolean true if the player was disconnected
     */
    private boolean reconnecting;

    /**
     * Constructor
     * @param user string name
     */
    public LoginMessage(String user) {
        this.nickname = user;
        this.reconnecting = false;
    }

    /**
     * Constructor
     * @param nickname name
     * @param reconnecting if he is trying to reconnect
     */
    public LoginMessage(String nickname, boolean reconnecting) {
        this.nickname = nickname;
        this.reconnecting = reconnecting;
    }

    /**
     *
     * @return the name
     */
    public String getNickname() {
        return nickname;
    }

    /**
     *
     * @return boolean
     */
    public boolean isReconnecting() {
        return reconnecting;
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
