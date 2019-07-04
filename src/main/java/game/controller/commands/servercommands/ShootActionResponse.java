package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;
import game.model.CardWeapon;
import game.model.Color;

import java.util.List;

/**
 * Send the correct application of the shoot action, and set the weapon to not loaded
 */
public class ShootActionResponse implements ServerGameMessage {
    /**
     * Weapon selected for the action
     */
    private CardWeapon selectedWeapon;

    /**
     * Constructor
     * @param selectedWeapon card weapon selected
     */
    public ShootActionResponse(CardWeapon selectedWeapon) {
        this. selectedWeapon = selectedWeapon;
    }

    /**
     *
     * @return return the weapon selected
     */
    public CardWeapon getSelectedWeapon() {
        return selectedWeapon;
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
