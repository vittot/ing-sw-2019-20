package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Action;
import game.model.CardAmmo;
import game.model.CardWeapon;
import game.model.Player;

import java.util.ArrayList;
import java.util.List;

public class NotifyGrabWeapon implements ServerMessage {
    private int p;
    private CardWeapon cw;
    private int x;
    private int y;
    private CardWeapon wWaste;

    public NotifyGrabWeapon(int p, CardWeapon cw, int x, int y) {
        this.p = p;
        this.cw = cw;
        this.x = x;
        this.y = y;
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
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}