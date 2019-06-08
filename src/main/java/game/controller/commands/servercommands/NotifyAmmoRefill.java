package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.CardAmmo;
import game.model.Square;
import game.model.exceptions.MapOutOfLimitException;

public class NotifyAmmoRefill implements ServerMessage {


    private CardAmmo ca;
    private Square position;

    public NotifyAmmoRefill(CardAmmo ca, Square position) {
        this.ca = ca;
        this.position = position;
    }

    public CardAmmo getCa() {
        return ca;
    }

    public Square getPosition() {
        return position;
    }

    @Override
    public void handle(ServerMessageHandler handler) throws MapOutOfLimitException {
        handler.handle(this);
    }
}
