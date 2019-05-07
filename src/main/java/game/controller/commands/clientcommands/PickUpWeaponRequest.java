package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.CardPower;
import game.model.CardWeapon;

import java.util.List;

public class PickUpWeaponRequest implements ClientMessage {
    public CardWeapon weapon;
    public List<CardPower> powerup;
    public CardWeapon weaponToWaste;
    @Override
    public ServerMessage handle(ClientMessageHandler handler) {
        return handler.handle(this);
    }
}
