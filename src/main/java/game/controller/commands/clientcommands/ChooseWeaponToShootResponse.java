package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardWeapon;

/**
 * resturn which weapon the player decide to shoot
 */
public class ChooseWeaponToShootResponse implements ClientGameMessage {
    /**
     * Card weapon selected
     */
    private CardWeapon selectedWeapon;

    /**
     * Constructor
     * @param selectedWeapon the wepoan selected
     */
    public ChooseWeaponToShootResponse(CardWeapon selectedWeapon) {
        this.selectedWeapon = selectedWeapon;
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
     * @return the weapon
     */
    public CardWeapon getSelectedWeapon() {
        return selectedWeapon;
    }
}
