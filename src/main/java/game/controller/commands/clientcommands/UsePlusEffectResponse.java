package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardPower;
import game.model.effects.FullEffect;

import java.util.List;

public class UsePlusEffectResponse implements ClientGameMessage {

    private List<FullEffect> plusRemained;
    private List<CardPower> toUse;
    private FullEffect effectToApply;

    public UsePlusEffectResponse(List<FullEffect> plusRemained, FullEffect effectToApply, List<CardPower> toUse) {
        this.effectToApply = effectToApply;
        this.plusRemained = plusRemained;
        this.toUse = toUse;
    }

    public List<FullEffect> getPlusRemained() {
        return plusRemained;
    }

    public FullEffect getEffectToApply() {
        return effectToApply;
    }

    public List<CardPower> getToUse() {
        return toUse;
    }

    @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }

    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
