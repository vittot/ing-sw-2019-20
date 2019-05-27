package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.effects.FullEffect;
import game.model.exceptions.NoCardAmmoAvailableException;

import java.util.List;

public class UseOrderPlusResponse implements ClientMessage {
    private List<FullEffect> plusEffects;
    private int i;
    private char t;

    public UseOrderPlusResponse(List<FullEffect> plusEffects, int i, char t) {
        this.plusEffects = plusEffects;
        this.i = i;
        this.t = t;
    }

    public List<FullEffect> getPlusEffects() {
        return plusEffects;
    }

    public int getI() {
        return i;
    }

    public char getT() {
        return t;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) throws NoCardAmmoAvailableException {
        return handler.handle(this);
    }
}
