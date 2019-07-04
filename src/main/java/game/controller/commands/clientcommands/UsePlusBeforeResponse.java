package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardPower;
import game.model.effects.FullEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * Response if the player want to use the effect before the base effect
 */
public class UsePlusBeforeResponse implements ClientGameMessage {
    /**
     * Possible effect before the base effect
     */
    private FullEffect plusEff;
    /**
     * power up used to pay
     */
    private List<CardPower> toUse;
    /**
     * His decision, 'y' for yes, 'n' for no
     */
    private char t;

    /**
     * Constructor
     * @param plusEff effect
     * @param t decision
     * @param toUse list of power up
     */
    public UsePlusBeforeResponse(FullEffect plusEff, char t, List<CardPower> toUse) {
        this.plusEff = plusEff;
        this.toUse = toUse;
        this.t = t;
    }

    /**
     *
     * @return effect selected
     */
    public FullEffect getPlusEff() {
        return plusEff;
    }

    /**
     *
     * @return decision
     */
    public char getT() {
        return t;
    }

    /**
     *
     * @return power up to pay
     */
    public List<CardPower> getToUse() {
        if(toUse != null)
            return toUse;
        return new ArrayList<>();
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
