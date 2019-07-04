package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardPower;
import game.model.CardWeapon;

import java.util.ArrayList;
import java.util.List;

/**
 * Request of reload a selected weapon
 */
public class ReloadWeaponRequest implements ClientGameMessage {
    /**
     * weapon that he want to reload
     */
    private CardWeapon weapon;
    /**
     * Power up used to pay
     */
    private List<CardPower> powerups;

    /**
     * Constructor
     * @param weapon selected
     * @param powerups selected
     */
    public ReloadWeaponRequest(CardWeapon weapon, List<CardPower> powerups) {
        this.weapon = weapon;
        this.powerups = powerups;
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
     * @return list of power up
     */
    public List<CardPower> getPowerups() {
        if(powerups != null)
            return powerups;
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
}
