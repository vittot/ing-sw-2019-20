package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;

public class NotifyRespawn implements ServerMessage {

    private int pId;
    private int x;
    private int y;

    public NotifyRespawn(int pId, int x, int y) {
        this.pId = pId;
        this.x = x;
        this.y = y;
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

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }


}
