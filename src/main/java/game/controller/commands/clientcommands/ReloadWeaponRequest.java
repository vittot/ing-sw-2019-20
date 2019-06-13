package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.CardPower;
import game.model.CardWeapon;

import java.util.ArrayList;
import java.util.List;

public class ReloadWeaponRequest implements ClientMessage {
    private CardWeapon weapon;
    private List<CardPower> powerups;

    public ReloadWeaponRequest(CardWeapon weapon, List<CardPower> powerups) {
        this.weapon = weapon;
        this.powerups = powerups;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) {
        return handler.handle(this);
    }

    public CardWeapon getWeapon() {
        return weapon;
    }

    public List<CardPower> getPowerups() {
        if(powerups != null)
            return powerups;
        return new ArrayList<>();
    }
}
