package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;

public class LoginMessage implements ClientGameMessage {

    private String nickname;
    private boolean reconnecting;

    public LoginMessage(String user) {
        this.nickname = user;
        this.reconnecting = false;
    }

    public LoginMessage(String nickname, boolean reconnecting) {
        this.nickname = nickname;
        this.reconnecting = reconnecting;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isReconnecting() {
        return reconnecting;
    }

    @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }

    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
