package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.CardPower;
import game.model.effects.FullEffect;
import game.model.exceptions.InsufficientAmmoException;
import game.model.exceptions.NoCardAmmoAvailableException;

import java.util.List;

public class UseOrderPlusResponse implements ClientMessage {
    private List<FullEffect> plusEffects;
    private List<CardPower> toUse;
    private char t;

    public UseOrderPlusResponse(List<FullEffect> plusEffects, List<CardPower> toUse, char t) {
        this.plusEffects = plusEffects;
        this.toUse = toUse;
        this.t = t;
    }

    public List<FullEffect> getPlusEffects() {
        return plusEffects;
    }

    public char getT() {
        return t;
    }

    public List<CardPower> getToUse() {
        return toUse;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) throws NoCardAmmoAvailableException, InsufficientAmmoException {
        return handler.handle(this);
    }
}
