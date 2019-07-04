package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.effects.FullEffect;

import java.util.List;

/**
 * Ask if the player want to use the optional effect in order
 */
public class UsePlusByOrderRequest implements ServerGameMessage {
    /**
     * List of optional effect of the weapon
     */
    private List<FullEffect> plusEffects;

    /**
     * Constructor
     * @param plusEffects list of effect
     */
    public UsePlusByOrderRequest(List<FullEffect> plusEffects) {
        this.plusEffects = plusEffects;
    }

    /**
     *
     * @return the list of effect
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
