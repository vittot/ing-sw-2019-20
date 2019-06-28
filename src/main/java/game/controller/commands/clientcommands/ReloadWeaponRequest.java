package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardPower;
import game.model.CardWeapon;

import java.util.ArrayList;
import java.util.List;

public class ReloadWeaponRequest implements ClientGameMessage {
    private CardWeapon weapon;
    private List<CardPower> powerups;

    public ReloadWeaponRequest(CardWeapon weapon, List<CardPower> powerups) {
        this.weapon = weapon;
        this.powerups = powerups;
    }

     @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }

    public CardWeapon getWeapon() {
        return weapon;
    }

    public List<CardPower> getPowerups() {
        if(powerups != null)
            return powerups;
        return new ArrayList<>();
    }

    @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }
}
