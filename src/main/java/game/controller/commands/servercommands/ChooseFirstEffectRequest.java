package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.effects.FullEffect;

/**
 * message that allow the choice of a first effect to apply during the shoot action
 */
public class ChooseFirstEffectRequest implements ServerGameMessage {

    /**
     * weapon base effect
     */
    private FullEffect baseEff;
    /**
     * weapon alternative effect
     */
    private FullEffect altEff;

    /**
     * construct the correct message
     * @param baseEff
     * @param altEff
     */
    public ChooseFirstEffectRequest(FullEffect baseEff, FullEffect altEff) {
        this.baseEff = baseEff;
        this.altEff = altEff;
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

    /**
     * return the base effect
     * @return baseEff
     */
    public FullEffect getBaseEff() {
        return baseEff;
    }

    /**
     * return the alternative effect
     * @return altEff
     */
    public FullEffect getAltEff() {
        return altEff;
    }
}
