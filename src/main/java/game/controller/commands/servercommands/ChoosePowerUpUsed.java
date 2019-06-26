package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;

public class ChoosePowerUpUsed implements ServerMessage {

    private CardPower cardPower;

    public ChoosePowerUpUsed(CardPower cardPower) {
        this.cardPower = cardPower;
    }

    public CardPower getCardPower() {
        return cardPower;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
