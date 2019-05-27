package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.effects.FullEffect;
import game.model.exceptions.MapOutOfLimitException;

import java.util.List;

public class UsePlusEffectRequest implements ServerMessage {

    private List<FullEffect> plusEffects;

    public UsePlusEffectRequest(List<FullEffect> plusEffects) {
        this.plusEffects = plusEffects;
    }

    public List<FullEffect> getPlusEffects() {
        return plusEffects;
    }


    @Override
    public void handle(ServerMessageHandler handler) throws MapOutOfLimitException {
        handler.handle(this);
    }
}
