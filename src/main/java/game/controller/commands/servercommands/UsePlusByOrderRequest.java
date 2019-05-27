package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.effects.FullEffect;
import game.model.exceptions.MapOutOfLimitException;

import java.util.List;

public class UsePlusByOrderRequest implements ServerMessage {

    private List<FullEffect> plusEffects;
    private int i;

    public UsePlusByOrderRequest(List<FullEffect> plusEffects, int i) {
        this.plusEffects = plusEffects;
        this.i = i;
    }

    public List<FullEffect> getPlusEffects() {
        return plusEffects;
    }

    public int getI() {
        return i;
    }

    @Override
    public void handle(ServerMessageHandler handler) throws MapOutOfLimitException {
        handler.handle(this);
    }
}
