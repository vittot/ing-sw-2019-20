package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;
import game.model.CardWeapon;

import java.util.List;

public class PickUpWeaponResponse implements ServerMessage {
    private CardWeapon cw;
    private CardWeapon cwToWaste;
    private List<CardPower> cp;

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

    public CardWeapon getCw() {
        return cw;
    }

    public CardWeapon getCwToWaste() {
        return cwToWaste;
    }

    public List<CardPower> getCp() {
        return cp;
    }
}
