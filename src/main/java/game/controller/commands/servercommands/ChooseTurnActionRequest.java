package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

public class ChooseTurnActionRequest implements ServerGameMessage {

    private boolean movAllowed;

    public ChooseTurnActionRequest(boolean movAllowed) {
        this.movAllowed = movAllowed;
    }

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

    public boolean isMovAllowed() {
        return movAllowed;
    }
}
