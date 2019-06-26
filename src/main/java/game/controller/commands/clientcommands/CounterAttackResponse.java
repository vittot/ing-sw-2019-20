package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.CardPower;
import game.model.Player;

public class CounterAttackResponse implements ClientMessage {

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
    public ServerMessage handle(ClientMessageHandler handler) {
        return handler.handle(this);
    }
}
