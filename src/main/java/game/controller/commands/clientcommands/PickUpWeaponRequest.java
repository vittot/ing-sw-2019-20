package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardPower;
import game.model.CardWeapon;

import java.util.List;

/**
 * Request of picking the wepoan in player position
 */
public class PickUpWeaponRequest implements ClientGameMessage {
    /**
     * weapon that the player want to pick up
     */
    private CardWeapon weapon;
    /**
     * Pouwer up used to pay
     */
    private List<CardPower> powerup;
    /**
     * Possible weapon to leave if the player has 3 weapon
     */
    private CardWeapon weaponToWaste;

    /**
     * Constructor
     * @param weapon card weapon
     * @param powerup list
     * @param weaponToWaste card weapon
     */
    public PickUpWeaponRequest(CardWeapon weapon, List<CardPower> powerup, CardWeapon weaponToWaste) {
        this.weapon = weapon;
        this.powerup = powerup;
        this.weaponToWaste = weaponToWaste;
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

    /**
     *
     * @return the weapon selected
     */
    public CardWeapon getWeapon() {
        return weapon;
    }

    /**
     *
     * @return the power up selected
     */
    public List<CardPower> getPowerup() {
        return powerup;
    }

    /**
     * the weapon to waste
     * @return
     */
    public CardWeapon getWeaponToWaste() {
        return weaponToWaste;
    }
}
