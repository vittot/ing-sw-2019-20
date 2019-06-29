package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;
import game.model.Color;

import java.util.ArrayList;
import java.util.List;

public class AddPayment implements ServerGameMessage {

    private List<Color> ammo;
    private List <CardPower> powers;

    public AddPayment(List<Color> ammo, List<CardPower> powers) {
        this.ammo = ammo;
        this.powers = powers;
    }

    public List<Color> getAmmo() {
        return ammo;
    }

    public List<CardPower> getPowers() {
        return powers;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }
}
