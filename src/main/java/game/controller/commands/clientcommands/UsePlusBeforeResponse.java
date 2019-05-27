package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.effects.FullEffect;
import game.model.exceptions.NoCardAmmoAvailableException;

public class UsePlusBeforeResponse implements ClientMessage {

    private FullEffect plusEff;
    private char t;

    public UsePlusBeforeResponse(FullEffect plusEff, char t) {
        this.plusEff = plusEff;
        this.t = t;
    }

    public FullEffect getPlusEff() {
        return plusEff;
    }

    public char getT() {
        return t;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) throws NoCardAmmoAvailableException {
        return null;
    }
}
