package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardWeapon;

public class ChooseWeaponToShootResponse implements ClientGameMessage {

    private CardWeapon selectedWeapon;

    public ChooseWeaponToShootResponse(CardWeapon selectedWeapon) {
        this.selectedWeapon = selectedWeapon;
    }

    @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }

    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }

    public CardWeapon getSelectedWeapon() {
        return selectedWeapon;
    }
}
