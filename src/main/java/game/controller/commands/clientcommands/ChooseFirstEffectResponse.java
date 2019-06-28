package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardPower;

import java.util.List;

public class ChooseFirstEffectResponse implements ClientGameMessage {

    private List<CardPower> toUse;
    private int n;

    public ChooseFirstEffectResponse(int n, List<CardPower> toUse) {
        this.toUse = toUse;
        this.n = n;
    }

    @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }

    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }

    public int getN() {
        return n;
    }

    public List<CardPower> getToUse() {
        return toUse;
    }
}
