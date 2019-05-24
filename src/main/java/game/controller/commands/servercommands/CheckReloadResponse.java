package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;
import game.model.CardWeapon;

import java.util.List;

public class CheckReloadResponse implements ServerMessage {
    private CardWeapon weapons;
    private List<CardPower> powerUps;

    public CheckReloadResponse(CardWeapon weapon, List<CardPower> powerUps) {
        this.powerUps = powerUps;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

    public CardWeapon getWeapon() {
        return weapons;
    }

    public List<CardPower> getPowerUps() {
        return powerUps;
    }
}
