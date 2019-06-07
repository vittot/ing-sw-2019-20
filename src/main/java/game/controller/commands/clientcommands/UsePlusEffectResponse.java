package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.CardPower;
import game.model.effects.FullEffect;
import game.model.exceptions.InsufficientAmmoException;
import game.model.exceptions.NoCardAmmoAvailableException;

import java.util.ArrayList;
import java.util.List;

public class UsePlusEffectResponse implements ClientMessage {

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
        if(toUse == null)
            return new ArrayList<>();
        return toUse;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) throws NoCardAmmoAvailableException, InsufficientAmmoException {
        return handler.handle(this);
    }
}
