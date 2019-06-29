package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;
import game.model.CardWeapon;
import game.model.Color;

import java.util.List;

public class ShootActionResponse implements ServerGameMessage {

    private CardWeapon selectedWeapon;

    public ShootActionResponse(CardWeapon selectedWeapon) {
        this. selectedWeapon = selectedWeapon;
    }

    public CardWeapon getSelectedWeapon() {
        return selectedWeapon;
    }

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
