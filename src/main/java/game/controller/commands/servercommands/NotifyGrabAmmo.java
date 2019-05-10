package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Color;
import game.model.Player;

import java.util.List;

public class NotifyGrabAmmo implements ServerMessage {

    private int pId;
    private int x;
    private int y;
    private List<Color> ammos;

    public NotifyGrabAmmo(int pId, int x, int y, List<Color> ammos) {
        this.pId = pId;
        this.x = x;
        this.y = y;
        this.ammos = ammos;
    }

    public int getP() {
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
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
