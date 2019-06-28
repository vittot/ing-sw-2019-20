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
    private List<Color> ammoToPay;
    private List<CardPower> poweupToPay;

    public ShootActionResponse(CardWeapon selectedWeapon, List<Color> ammoToPay, List<CardPower> powerUpToPay) {
        this. selectedWeapon = selectedWeapon;
        this.ammoToPay = ammoToPay;
        this.poweupToPay = powerUpToPay;
    }

    public CardWeapon getSelectedWeapon() {
        return selectedWeapon;
    }

    public List<Color> getAmmoToPay() {
        return ammoToPay;
    }

    public List<CardPower> getPoweupToPay() {
        return poweupToPay;
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
