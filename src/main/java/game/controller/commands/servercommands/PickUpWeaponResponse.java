package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;
import game.model.CardWeapon;

import java.util.List;

public class PickUpWeaponResponse implements ServerMessage {
    public CardWeapon cw;
    public CardWeapon cwToWaste;
    public List<CardPower> cp;

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
