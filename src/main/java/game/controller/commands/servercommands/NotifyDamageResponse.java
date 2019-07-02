package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

public class NotifyDamageResponse implements ServerGameMessage {
    private int shooterId;
    private int hitId;
    private int damage;
    private int marksToRemove;

    public NotifyDamageResponse(int shooterId, int hitId, int damage, int marksToRemove) {
        this.shooterId = shooterId;
        this.hitId = hitId;
        this.damage = damage;
        this.marksToRemove = marksToRemove;
    }

    public int getShooterId() {
        return shooterId;
    }

    public int getHit() {
        return hitId;
    }

    public int getDamage() {
        return damage;
    }

    public int getMarksToRemove() {
        return marksToRemove;
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
