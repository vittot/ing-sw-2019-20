package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.effects.FullEffect;
import game.model.exceptions.MapOutOfLimitException;

public class BeforeBaseRequest implements ServerMessage {

    private FullEffect plusEff;

    public BeforeBaseRequest(FullEffect plusEff) {
        this.plusEff = plusEff;
    }

    public FullEffect getPlusEff() {
        return plusEff;
    }

    @Override
    public void handle(ServerMessageHandler handler) throws MapOutOfLimitException {
        handler.handle(this);
    }
}
