package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

public class NotifyMarks implements ServerGameMessage {

    private int shooterId;
    private int hitId;
    private int marks;

    public NotifyMarks(int shooterId, int hitId, int marks) {
        this.shooterId = shooterId;
        this.hitId = hitId;
        this.marks = marks;
    }

    public int getShooterId() {
        return shooterId;
    }

    public int getHitId() {
        return hitId;
    }

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
