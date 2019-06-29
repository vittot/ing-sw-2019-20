package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardAmmo;
import game.model.Square;

public class NotifyAmmoRefill implements ServerGameMessage {


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
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
