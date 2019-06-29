package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.effects.FullEffect;

public class ChooseFirstEffectRequest implements ServerGameMessage {

    private FullEffect baseEff;
    private FullEffect altEff;

    public ChooseFirstEffectRequest(FullEffect baseEff, FullEffect altEff) {
        this.baseEff = baseEff;
        this.altEff = altEff;
    }

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
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
