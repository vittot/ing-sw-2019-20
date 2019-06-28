package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;
import game.model.CardWeapon;

import java.util.List;

public class PickUpWeaponResponse implements ServerGameMessage {
    private CardWeapon cw;
    private CardWeapon cwToWaste;
    private List<CardPower> cp;

    public PickUpWeaponResponse(CardWeapon cw, CardWeapon cwToWaste, List<CardPower> cp) {
        this.cw = cw;
        this.cwToWaste = cwToWaste;
        this.cp = cp;
    }

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

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
