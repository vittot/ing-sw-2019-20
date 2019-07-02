package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.Kill;

public class NotifyDeathResponse implements ServerGameMessage {
    private int idKiller;
    private int idVictim;
    private boolean isRage;

    public NotifyDeathResponse(Kill kill) {
        this.idKiller = kill.getKiller().getId();
        this.idVictim = kill.getVictim().getId();
        this.isRage = kill.isRage();
    }

    public int getIdKiller() {
        return idKiller;
    }

    public int getIdVictim() {
        return idVictim;
    }

    public boolean isRage() {
        return isRage;
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

