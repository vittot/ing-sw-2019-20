package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.Kill;
import game.model.Player;

public class NotifyRage implements ServerGameMessage {
    private Player killer;
    private Player victim;

    public NotifyRage(Kill kill) {
        this.killer = kill.getKiller();
        this.victim = kill.getVictim();
    }

    public Player getKiller() {
        return killer;
    }

    public Player getVictim() {
        return victim;
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
