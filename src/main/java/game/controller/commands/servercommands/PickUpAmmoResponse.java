package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;
import game.model.Color;

import java.util.List;

public class PickUpAmmoResponse implements ServerGameMessage {

    private List<Color> colors;
    private List<CardPower> powerups;

    public PickUpAmmoResponse(List<Color> colors, List<CardPower> powerups) {
        this.colors = colors;
        this.powerups = powerups;
    }

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

    public List<Color> getColors() {
        return colors;
    }

    public List<CardPower> getPowerups() {
        return powerups;
    }
}
