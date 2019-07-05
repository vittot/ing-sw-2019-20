package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;
import game.model.CardWeapon;

import java.util.List;

/**
 * notify the correct reload of a weapon and the power-up cards used
 */
public class CheckReloadResponse implements ServerGameMessage {
    /**
     * weapon that has been reloaded
     */
    private CardWeapon weapon;

    /**
     * power-up cards to discard
     */
    private List<CardPower> powerUps;

    /**
     * construct the message
     * @param weapon
     * @param powerUps
     */
    public CheckReloadResponse(CardWeapon weapon, List<CardPower> powerUps) {
        this.weapon = weapon;
        this.powerUps = powerUps;
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
     * return the weapon reference
     * @return weapon
     */
    public CardWeapon getWeapon() {
        return weapon;
    }

    /**
     * return the power-up cards list
     * @return powerUps
     */
    public List<CardPower> getPowerUps() {
        return powerUps;
    }
}
