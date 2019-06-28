package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.Color;

import java.util.List;

public class NotifyGrabCardAmmo implements ServerGameMessage {

    private int pId;
    private int x;
    private int y;
    private List<Color> ammos;
    //private List<CardPower> powerUps;

    public NotifyGrabCardAmmo(int pId, int x, int y, List<Color> ammos) {
        this.pId = pId;
        this.x = x;
        this.y = y;
        this.ammos = ammos;
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

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
