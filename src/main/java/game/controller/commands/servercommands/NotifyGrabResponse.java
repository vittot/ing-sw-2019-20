package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Action;
import game.model.CardAmmo;
import game.model.CardWeapon;

import java.util.ArrayList;
import java.util.List;

public class NotifyGrabResponse implements ServerMessage {
    public int idp;
    public CardWeapon cw;
    public CardAmmo ca;
    public int x;
    public int y;

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}