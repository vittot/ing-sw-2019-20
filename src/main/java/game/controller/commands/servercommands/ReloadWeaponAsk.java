package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.CardWeapon;

import java.util.List;

public class ReloadWeaponAsk implements ServerMessage {

    private List<CardWeapon> weaponsToReload;

    public ReloadWeaponAsk(List<CardWeapon> weaponsToReload) {
        this.weaponsToReload = weaponsToReload;
    }

    public List<CardWeapon> getWeaponsToReload() {
        return weaponsToReload;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
