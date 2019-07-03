package game.controller.commands.clientcommands;

import game.controller.commands.*;

public class ReloadWeaponAction implements ClientGameMessage {

    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }
}
