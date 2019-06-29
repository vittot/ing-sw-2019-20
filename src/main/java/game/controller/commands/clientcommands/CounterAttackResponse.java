package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardPower;
import game.model.Player;

public class CounterAttackResponse implements ClientGameMessage {

    private CardPower cardPower;
    private Player toShoot;
    private boolean confirm;

    public CounterAttackResponse(CardPower cardPower, Player toShoot) {
        this.cardPower = cardPower;
        this.toShoot = toShoot;
        this.confirm = true;
    }

    public CounterAttackResponse() {
        this.confirm = false;
    }

    public CardPower getCardPower() {
        return cardPower;
    }

    public Player getToShoot() {
        return toShoot;
    }

    public boolean isConfirm() {
        return confirm;
    }

    @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }

    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
