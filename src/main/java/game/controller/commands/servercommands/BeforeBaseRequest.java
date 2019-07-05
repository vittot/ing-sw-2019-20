package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.effects.FullEffect;

/**
 * message that ask the client if he wants to apply a plus effect before base
 */
public class BeforeBaseRequest implements ServerGameMessage {
    /**
     * plus effect proposed
     */
    private FullEffect plusEff;

    /**
     * construct the message
     * @param plusEff
     */
    public BeforeBaseRequest(FullEffect plusEff) {
        this.plusEff = plusEff;
    }

    /**
     * return the plus effect proposed
     * @return plusEff
     */
    public FullEffect getPlusEff() {
        return plusEff;
    }

    /**
     * Handle the message
     * @param handler who handle the message
     * @return the message from the handler
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the message
     * @return the message from the handler
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
