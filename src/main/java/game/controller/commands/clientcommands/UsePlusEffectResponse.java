package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardPower;
import game.model.effects.FullEffect;

import java.util.List;

/**
 * Response if the player want to use the optional effect
 */
public class UsePlusEffectResponse implements ClientGameMessage {
    /**
     * List of effect that he can do
     */
    private List<FullEffect> plusRemained;
    /**
     * Power up to pay
     */
    private List<CardPower> toUse;
    /**
     * Effect to do
     */
    private FullEffect effectToApply;

    /**
     * Constructor
     * @param plusRemained other possible effect
     * @param effectToApply his decision
     * @param toUse list of power up
     */
    public UsePlusEffectResponse(List<FullEffect> plusRemained, FullEffect effectToApply, List<CardPower> toUse) {
        this.effectToApply = effectToApply;
        this.plusRemained = plusRemained;
        this.toUse = toUse;
    }

    /**
     *
     * @return the effect other choice
     */
    public List<FullEffect> getPlusRemained() {
        return plusRemained;
    }

    /**
     *
     * @return his effect choice
     */
    public FullEffect getEffectToApply() {
        return effectToApply;
    }

    /**
     *
     * @return list of power up to pay
     */
    public List<CardPower> getToUse() {
        return toUse;
    }

    /**
     * Handle the message
     * @param handler who handle the message
     * @return the message from the handler
     */
    @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the message
     * */
    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
