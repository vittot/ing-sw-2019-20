package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.effects.FullEffect;
import game.model.exceptions.NoCardAmmoAvailableException;

import java.util.List;

public class UsePlusEffectResponse implements ClientMessage {

    private List<FullEffect> plusRemained;
    private FullEffect effectToApply;

    public UsePlusEffectResponse(List<FullEffect> plusRemained, FullEffect effectToApply) {
        this.effectToApply = effectToApply;
        this.plusRemained = plusRemained;
    }

    public List<FullEffect> getPlusRemained() {
        return plusRemained;
    }

    public FullEffect getEffectToApply() {
        return effectToApply;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) throws NoCardAmmoAvailableException {
        return handler.handle(this);
    }
}
