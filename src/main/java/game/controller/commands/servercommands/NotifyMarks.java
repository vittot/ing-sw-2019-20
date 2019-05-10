package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Player;

public class NotifyMarks implements ServerMessage {

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

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
