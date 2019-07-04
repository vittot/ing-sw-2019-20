package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;
import game.model.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * message that contains the payment executed on the server (ammos and power-up cards to remove)
 */
public class AddPayment implements ServerGameMessage {

    private List<Color> ammo; /** ammo to pay */
    private List <CardPower> powers; /** power-up cards to pay */

    /**
     * construct he correct message
     * @param ammo
     * @param powers
     */
    public AddPayment(List<Color> ammo, List<CardPower> powers) {
        this.ammo = ammo;
        this.powers = powers;
    }

    /**
     * return ammo list
     * @return ammo
     */
    public List<Color> getAmmo() {
        return ammo;
    }

    /**
     * return power-up list
     * @return powers
     */
    public List<CardPower> getPowers() {
        return powers;
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
     * Handle the message
     * @param handler who handle the message
     * @return the message from the handler
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }
}
