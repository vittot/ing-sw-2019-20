package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.Kill;

/**
 * notify player death
 */
public class NotifyDeathResponse implements ServerGameMessage {
    private int idKiller; /** killer player id */
    private int idVictim; /** victim player id */
    private boolean isRage; /** boolean value that specifies if the victim was raged */

    /**
     * construct correct message
     * @param kill
     */
    public NotifyDeathResponse(Kill kill) {
        this.idKiller = kill.getKiller().getId();
        this.idVictim = kill.getVictim().getId();
        this.isRage = kill.isRage();
    }

    /**
     * return killer id
     * @return idKiller
     */
    public int getIdKiller() {
        return idKiller;
    }

    /**
     * return victim id
     * @return idVictim
     */
    public int getIdVictim() {
        return idVictim;
    }

    /**
     * return if the victim was raged
     * @return isRage
     */
    public boolean isRage() {
        return isRage;
    }

    /**
     * Handle the message
     * @param handler who handle the messag
     * @param handler
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the messag
     * @param handler
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}

