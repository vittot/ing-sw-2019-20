package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.Player;

public class CheckValidWeaponRequst implements ClientMessage {
    private Player player;

    public CheckValidWeaponRequst(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) {
        return handler.handle(this);
    }
}
