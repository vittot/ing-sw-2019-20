package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

/**
 * notify the dealt of damage
 */
public class NotifyDamageResponse implements ServerGameMessage {

    private int shooterId; /** shooter's id that dealt damage */
    private int hitId; /** victim's id that dealt damage */
    private int damage; /** damages dealt */
    private int marksToRemove; /** marks to remove for the victim */

    /**
     * construct correct message
     * @param shooterId
     * @param hitId
     * @param damage
     * @param marksToRemove
     */
    public NotifyDamageResponse(int shooterId, int hitId, int damage, int marksToRemove) {
        this.shooterId = shooterId;
        this.hitId = hitId;
        this.damage = damage;
        this.marksToRemove = marksToRemove;
    }

    /**
     * return shooterId
     * @return shooterId
     */
    public int getShooterId() {
        return shooterId;
    }

    /**
     * return victimId
     * @return hitId
     */
    public int getHit() {
        return hitId;
    }

    /**
     * return damage value
     * @return damage
     */
    public int getDamage() {
        return damage;
    }

    /**
     * return number of marks
     * @return marksToRemove
     */
    public int getMarksToRemove() {
        return marksToRemove;
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
