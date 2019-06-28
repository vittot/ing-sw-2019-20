package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

public class NotifyDamageResponse implements ServerGameMessage {
    private int shooterId;
    private int hitId;
    private int damage;

    public NotifyDamageResponse(int shooterId, int hitId, int damage) {
        this.shooterId = shooterId;
        this.hitId = hitId;
        this.damage = damage;
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

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
