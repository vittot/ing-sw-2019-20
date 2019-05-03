package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.model.CardPower;
import game.model.CardWeapon;

import java.util.List;

public class PickUpWeaponRequest implements ClientMessage {
    public CardWeapon weapon;
    public List<CardPower> powerup;
    public CardWeapon weaponToWaste;
    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
