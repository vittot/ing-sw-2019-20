package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.CardPower;
import game.model.effects.FullEffect;
import game.model.exceptions.NoCardAmmoAvailableException;

import java.util.List;

public class UsePlusBeforeResponse implements ClientMessage {

    private FullEffect plusEff;
    private List<CardPower> toUse;
    private char t;

    public UsePlusBeforeResponse(FullEffect plusEff, char t, List<CardPower> toUse) {
        this.plusEff = plusEff;
        this.toUse = toUse;
        this.t = t;
    }

    public FullEffect getPlusEff() {
        return plusEff;
    }

    public char getT() {
        return t;
    }

    public List<CardPower> getToUse() {
        return toUse;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) {
        return null;
    }
}
