package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.exceptions.NoCardAmmoAvailableException;

public class ChooseFirstEffectResponse implements ClientMessage {

    private int n;

    public ChooseFirstEffectResponse(int n) {
        this.n = n;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) throws NoCardAmmoAvailableException {
        return handler.handle(this);
    }

    public int getN() {
        return n;
    }
}
