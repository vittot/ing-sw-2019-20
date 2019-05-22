package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.CardPower;
import game.model.CardWeapon;

import java.util.List;

public class PickUpWeaponRequest implements ClientMessage {
    private CardWeapon weapon;
    private List<CardPower> powerup;
    private CardWeapon weaponToWaste;

    public PickUpWeaponRequest(CardWeapon weapon, List<CardPower> powerup, CardWeapon weaponToWaste) {
        this.weapon = weapon;
        this.powerup = powerup;
        this.weaponToWaste = weaponToWaste;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) {
        return handler.handle(this);
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
