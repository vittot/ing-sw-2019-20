package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.CardPower;
import game.model.exceptions.InsufficientAmmoException;
import game.model.exceptions.NoCardAmmoAvailableException;

import java.util.ArrayList;
import java.util.List;

public class ChooseFirstEffectResponse implements ClientMessage {

    private List<CardPower> toUse;
    private int n;

    public ChooseFirstEffectResponse(int n, List<CardPower> toUse) {
        this.toUse = toUse;
        this.n = n;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) {
        return handler.handle(this);
    }

    public int getN() {
        return n;
    }

    public List<CardPower> getToUse() {
        if(toUse != null)
            return toUse;
        return new ArrayList<>();
    }
}
