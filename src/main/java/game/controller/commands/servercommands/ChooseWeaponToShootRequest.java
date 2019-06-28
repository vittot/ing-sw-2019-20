package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardWeapon;

import java.util.List;

public class ChooseWeaponToShootRequest implements ServerGameMessage {

    private List<CardWeapon> myWeapons;

    public ChooseWeaponToShootRequest(List<CardWeapon> myWeapons) {
        this.myWeapons = myWeapons;
    }

    public List<CardWeapon> getMyWeapons() {
        return myWeapons;
    }

    @Override
    public void handle(ServerGameMessageHandler handler)  {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
