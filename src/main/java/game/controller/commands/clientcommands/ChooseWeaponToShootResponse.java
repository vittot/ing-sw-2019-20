package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.CardWeapon;
import game.model.exceptions.NoCardAmmoAvailableException;

public class ChooseWeaponToShootResponse implements ClientMessage {

    private CardWeapon selectedWeapon;

    public ChooseWeaponToShootResponse(CardWeapon selectedWeapon) {
        this.selectedWeapon = selectedWeapon;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) {
        return handler.handle(this);
    }

    public CardWeapon getSelectedWeapon() {
        return selectedWeapon;
    }
}
