package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.effects.FullEffect;

public class BeforeBaseRequest implements ServerGameMessage {

    private FullEffect plusEff;

    public BeforeBaseRequest(FullEffect plusEff) {
        this.plusEff = plusEff;
    }

    public FullEffect getPlusEff() {
        return plusEff;
    }

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
