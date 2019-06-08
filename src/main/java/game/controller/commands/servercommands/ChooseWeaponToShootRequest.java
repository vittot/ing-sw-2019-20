package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.CardWeapon;
import game.model.exceptions.MapOutOfLimitException;

import java.util.List;

public class ChooseWeaponToShootRequest implements ServerMessage {

    private List<CardWeapon> myWeapons;

    public ChooseWeaponToShootRequest(List<CardWeapon> myWeapons) {
        this.myWeapons = myWeapons;
    }

    public List<CardWeapon> getMyWeapons() {
        return myWeapons;
    }

    @Override
    public void handle(ServerMessageHandler handler)  {
        handler.handle(this);
    }
}
