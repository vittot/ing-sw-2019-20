package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.effects.FullEffect;
import game.model.exceptions.MapOutOfLimitException;

public class ChooseFirstEffectRequest implements ServerMessage {

    private FullEffect baseEff;
    private FullEffect altEff;

    public ChooseFirstEffectRequest(FullEffect baseEff, FullEffect altEff) {
        this.baseEff = baseEff;
        this.altEff = altEff;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

    public FullEffect getBaseEff() {
        return baseEff;
    }

    public FullEffect getAltEff() {
        return altEff;
    }
}
