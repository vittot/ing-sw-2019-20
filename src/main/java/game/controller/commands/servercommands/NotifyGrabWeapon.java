package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardWeapon;

public class NotifyGrabWeapon implements ServerGameMessage {
    private int p;
    private CardWeapon cw;
    private int x;
    private int y;
    private CardWeapon wWaste;

    public NotifyGrabWeapon(int p, CardWeapon cw, int x, int y, CardWeapon wWaste) {
        this.p = p;
        this.cw = cw;
        this.x = x;
        this.y = y;
        this.wWaste = wWaste;
    }

    public CardWeapon getwWaste() {
        return wWaste;
    }

    public void setwWaste(CardWeapon wWaste) {
        this.wWaste = wWaste;
    }

    public int getP() {
        return p;
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

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}