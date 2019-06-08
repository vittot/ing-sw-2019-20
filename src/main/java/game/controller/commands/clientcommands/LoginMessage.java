package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.exceptions.NoCardAmmoAvailableException;

public class LoginMessage implements ClientMessage {

    private String nickname;

    public LoginMessage(String user) {
        this.nickname = user;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) {
        return handler.handle(this);
}
}
