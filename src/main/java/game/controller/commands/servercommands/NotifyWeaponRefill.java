package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.CardWeapon;
import game.model.Square;
import game.model.exceptions.MapOutOfLimitException;

public class NotifyWeaponRefill implements ServerMessage {

    private CardWeapon cw;
    private Square position;

    public NotifyWeaponRefill(CardWeapon cw, Square position) {
        this.cw = cw;
        this.position = position;
    }

    public CardWeapon getCw() {
        return cw;
    }

    public Square getPosition() {
        return position;
    }

    @Override
    public void handle(ServerMessageHandler handler) throws MapOutOfLimitException {
        handler.handle(this);
    }
}
