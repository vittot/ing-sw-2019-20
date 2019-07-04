package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

public class NotifyRespawn implements ServerGameMessage {

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

    /**
     * Handle the message
     * @param handler who handle the messag
     * @param handler
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the messag
     * @param handler
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }


}
