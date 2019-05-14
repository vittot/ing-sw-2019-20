package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.CardPower;
import game.model.CardWeapon;

import java.util.List;

public class ReloadWeaponRequest implements ClientMessage {
    private CardWeapon weapon;
    private List<CardPower> powerups;

    @Override
    public ServerMessage handle(ClientMessageHandler handler) {
        return handler.handle(this);
    }

    public CardWeapon getWeapon() {
        return weapon;
    }

    public List<CardPower> getPowerups() {
        return powerups;
    }
}
