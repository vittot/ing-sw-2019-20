package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;
import game.model.Color;

import java.util.List;

/**
 * Send the confirm operation for the grab ammo
 */
public class PickUpAmmoResponse implements ServerGameMessage {
    /**
     * List of color in the ammo card grabbed
     */
    private List<Color> colors;
    /**
     * list of power up in the card
     */
    private List<CardPower> powerups;

    /**
     * Constructor
     * @param colors list of color
     * @param powerups list of power up
     */
    public PickUpAmmoResponse(List<Color> colors, List<CardPower> powerups) {
        this.colors = colors;
        this.powerups = powerups;
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

    /**
     *
     * @return color in the card
     */
    public List<Color> getColors() {
        return colors;
    }

    /**
     *
     * @return power up in the card
     */
    public List<CardPower> getPowerups() {
        return powerups;
    }
}
