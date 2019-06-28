package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;
import game.model.CardWeapon;
import game.model.Color;
import game.model.exceptions.InsufficientAmmoException;
import game.model.exceptions.MapOutOfLimitException;

import java.util.List;

public class ShootActionResponse implements ServerMessage {

    private CardWeapon selectedWeapon;

    public ShootActionResponse(CardWeapon selectedWeapon) {
        this. selectedWeapon = selectedWeapon;
    }

    public CardWeapon getSelectedWeapon() {
        return selectedWeapon;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
