package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

/**
 * notify the marks given to a certain player
 */
public class NotifyMarks implements ServerGameMessage {

    private int shooterId; /** shooter id */
    private int hitId; /** victim id */
    private int marks; /** number of marks given/received */

    /**
     * construct correct message
     * @param shooterId
     * @param hitId
     * @param marks
     */
    public NotifyMarks(int shooterId, int hitId, int marks) {
        this.shooterId = shooterId;
        this.hitId = hitId;
        this.marks = marks;
    }

    /**
     * return the shooter id
     * @return shooterId
     */
    public int getShooterId() {
        return shooterId;
    }

    /**
     * return the victim id
     * @return hitId
     */
    public int getHitId() {
        return hitId;
    }

    /**
     * return marks
     * @return marks
     */
    public int getMarks() {
        return marks;
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
