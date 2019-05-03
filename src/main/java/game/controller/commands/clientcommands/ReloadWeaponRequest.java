package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.model.CardPower;
import game.model.CardWeapon;

import java.util.List;

public class ReloadWeaponRequest implements ClientMessage {
    public CardWeapon weapon;
    public List<CardPower> powerups;

    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
