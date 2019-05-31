package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.exceptions.NoCardAmmoAvailableException;

public class RejoinGameResponse implements ClientMessage {

    private boolean rejoin;
    private String user;

    public RejoinGameResponse(boolean rejoin,String user) {
        this.rejoin = rejoin;
        this.user = user;
    }

    public boolean isRejoin() {
        return rejoin;
    }

    public String getUser() {
        return user;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) throws NoCardAmmoAvailableException {
        return handler.handle(this);
    }
}
