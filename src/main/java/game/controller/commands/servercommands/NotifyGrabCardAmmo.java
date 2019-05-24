package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;
import game.model.Color;
import game.model.Player;
import game.model.exceptions.MapOutOfLimitException;

import java.util.List;

public class NotifyGrabCardAmmo implements ServerMessage {

    private int pId;
    private int x;
    private int y;
    private List<Color> ammos;
    private List<CardPower> powerUps;

    public NotifyGrabCardAmmo(int pId, int x, int y, List<Color> ammos) {
        this.pId = pId;
        this.x = x;
        this.y = y;
        this.ammos = ammos;
        this.powerUps = powerUps;
    }

    public int getpId() {
        return pId;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public List<Color> getAmmos() {
        return ammos;
    }

    public List<CardPower> getPowerUps() {
        return powerUps;
    }

    @Override
    public void handle(ServerMessageHandler handler) throws MapOutOfLimitException {
        handler.handle(this);
    }
}
