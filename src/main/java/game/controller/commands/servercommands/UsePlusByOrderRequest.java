package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.effects.FullEffect;
import game.model.exceptions.MapOutOfLimitException;

import java.util.List;

public class UsePlusByOrderRequest implements ServerMessage {

    private List<FullEffect> plusEffects;

    public UsePlusByOrderRequest(List<FullEffect> plusEffects) {
        this.plusEffects = plusEffects;
    }

    public List<FullEffect> getPlusEffects() {
        return plusEffects;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
