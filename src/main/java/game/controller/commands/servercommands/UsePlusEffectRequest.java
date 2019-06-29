package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.effects.FullEffect;

import java.util.List;

public class UsePlusEffectRequest implements ServerGameMessage {

    private List<FullEffect> plusEffects;

    public UsePlusEffectRequest(List<FullEffect> plusEffects) {
        this.plusEffects = plusEffects;
    }

    public List<FullEffect> getPlusEffects() {
        return plusEffects;
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
