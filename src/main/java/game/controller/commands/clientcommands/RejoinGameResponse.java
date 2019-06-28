package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;

public class RejoinGameResponse implements ClientGameMessage {

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
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }

    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
