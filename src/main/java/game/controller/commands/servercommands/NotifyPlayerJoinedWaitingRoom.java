package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Player;
import game.model.exceptions.MapOutOfLimitException;

public class NotifyPlayerJoinedWaitingRoom implements ServerMessage {

    private Player player;

    public NotifyPlayerJoinedWaitingRoom(Player p) {
        this.player = p;
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
