package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardWeapon;

import java.util.List;

/**
 * Ask which weapon the player want to reload, if he want to
 */
public class ReloadWeaponAsk implements ServerGameMessage {
    /**
     * Possible card weapon to reload
     */
    private List<CardWeapon> weaponsToReload;

    /**
     * Constructor
     * @param weaponsToReload list of weapon
     */
    public ReloadWeaponAsk(List<CardWeapon> weaponsToReload) {
        this.weaponsToReload = weaponsToReload;
    }

    /**
     *
     * @return the possible weapon to reload
     */
    public List<CardWeapon> getWeaponsToReload() {
        return weaponsToReload;
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
