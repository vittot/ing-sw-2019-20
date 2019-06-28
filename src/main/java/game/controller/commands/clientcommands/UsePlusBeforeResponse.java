package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardPower;
import game.model.effects.FullEffect;

import java.util.ArrayList;
import java.util.List;

public class UsePlusBeforeResponse implements ClientGameMessage {

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
        if(toUse != null)
            return toUse;
        return new ArrayList<>();
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
