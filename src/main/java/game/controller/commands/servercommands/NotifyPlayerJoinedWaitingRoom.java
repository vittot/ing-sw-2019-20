package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.Player;

public class NotifyPlayerJoinedWaitingRoom implements ServerGameMessage {

    private Player player;

    public NotifyPlayerJoinedWaitingRoom(Player p) {
        this.player = p;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
