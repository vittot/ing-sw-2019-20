package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardPower;
import game.model.effects.FullEffect;

import java.util.List;

/**
 * Response from the player if he want to use the optional effect in order
 */
public class UseOrderPlusResponse implements ClientGameMessage {
    /**
     * Listo of the optional effect
     */
    private List<FullEffect> plusEffects;
    /**
     * List of card power to pay
     */
    private List<CardPower> toUse;
    /**
     * 'y' is he want, 'n' is he doesn't want
     */
    private char t;

    /**
     * Constructor
     * @param plusEffects list of effect
     * @param toUse power up to pay
     * @param t decision
     */
    public UseOrderPlusResponse(List<FullEffect> plusEffects, List<CardPower> toUse, char t) {
        this.plusEffects = plusEffects;
        this.toUse = toUse;
        this.t = t;
    }

    /**
     *
     * @return the effects
     */
    public List<FullEffect> getPlusEffects() {
        return plusEffects;
    }

    /**
     *
     * @return the decision
     */
    public char getT() {
        return t;
    }

    /**
     *
     * @return power up to pay
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
     */
    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
