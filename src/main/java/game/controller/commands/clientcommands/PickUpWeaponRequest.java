package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardPower;
import game.model.CardWeapon;

import java.util.List;

public class PickUpWeaponRequest implements ClientGameMessage {
    private CardWeapon weapon;
    private List<CardPower> powerup;
    private CardWeapon weaponToWaste;

    public PickUpWeaponRequest(CardWeapon weapon, List<CardPower> powerup, CardWeapon weaponToWaste) {
        this.weapon = weapon;
        this.powerup = powerup;
        this.weaponToWaste = weaponToWaste;
    }

    @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }

    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }

    public CardWeapon getWeapon() {
        return weapon;
    }

    public List<CardPower> getPowerup() {
        return powerup;
    }

    public CardWeapon getWeaponToWaste() {
        return weaponToWaste;
    }
}
