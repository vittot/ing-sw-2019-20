package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.effects.FullEffect;

import java.util.List;

/**
 * Ask the client which plus effect the player want to use
 */
public class UsePlusEffectRequest implements ServerGameMessage {
    /**
     * List of possible effect
     */
    private List<FullEffect> plusEffects;

    /**
     * Constructor
     * @param plusEffects list of effect
     */
    public UsePlusEffectRequest(List<FullEffect> plusEffects) {
        this.plusEffects = plusEffects;
    }

    /**
     *
     * @return the plus effect list
     */
    public List<FullEffect> getPlusEffects() {
        return plusEffects;
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }
    /**
     * Handle the message
     * @param handler who handle the message
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
