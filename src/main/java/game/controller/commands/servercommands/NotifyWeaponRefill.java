package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardWeapon;
import game.model.Square;

public class NotifyWeaponRefill implements ServerGameMessage {

    private CardWeapon cw;
    private int x;
    private int y;

    public NotifyWeaponRefill(CardWeapon cw, Square position) {
        this.cw = cw;
        this.x = position.getX();
        this.y = position.getY();
    }

    public CardWeapon getCw() {
        return cw;
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
