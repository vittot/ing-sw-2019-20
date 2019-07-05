package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;
import game.model.CardWeapon;

import java.util.List;

/**
 * Confirm to the client that the grab action is correctly applied
 */
public class PickUpWeaponResponse implements ServerGameMessage {
    /**
     * card weapon grabbed
     */
    private CardWeapon cw;
    /**
     * card weapon wasted, can be null
     */
    private CardWeapon cwToWaste;
    /**
     * list of card power to pay
     */
    private List<CardPower> cp;

    /**
     * Constructor
     * @param cw card weapon
     * @param cwToWaste card to waste
     * @param cp power up to pay
     */
    public PickUpWeaponResponse(CardWeapon cw, CardWeapon cwToWaste, List<CardPower> cp) {
        this.cw = cw;
        this.cwToWaste = cwToWaste;
        this.cp = cp;
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

    /**
     *
     * @return the weapon to grab
     */
    public CardWeapon getCw() {
        return cw;
    }

    /**
     *
     * @return the weapon to waste
     */
    public CardWeapon getCwToWaste() {
        return cwToWaste;
    }

    /**
     *
     * @return the card power to pay
     */
    public List<CardPower> getCp() {
        return cp;
    }
}
