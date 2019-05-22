package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.CardWeapon;

import java.util.List;

public class ChooseWeaponToGrabRequest implements ServerMessage{

    List<CardWeapon> weapons;

    public ChooseWeaponToGrabRequest(List<CardWeapon> weapons) {
        this.weapons = weapons;
    }

    public List<CardWeapon> getWeapons() {
        return weapons;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
