package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardWeapon;

import java.util.List;

public class ChooseWeaponToGrabRequest implements ServerGameMessage {

    List<CardWeapon> weapons;

    public ChooseWeaponToGrabRequest(List<CardWeapon> weapons) {
        this.weapons = weapons;
    }

    public List<CardWeapon> getWeapons() {
        return weapons;
    }

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
