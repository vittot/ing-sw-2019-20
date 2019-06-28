package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardPower;
import game.model.effects.FullEffect;

import java.util.List;

public class UseOrderPlusResponse implements ClientGameMessage {
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
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }

    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
