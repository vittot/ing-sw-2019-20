package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Player;
import game.model.Target;

import java.util.List;

public class NotifyDamageResponse implements ServerMessage {
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
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
