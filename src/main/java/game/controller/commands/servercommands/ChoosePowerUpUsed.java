package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;

public class ChoosePowerUpUsed implements ServerGameMessage {

    private CardPower cardPower;

    public ChoosePowerUpUsed(CardPower cardPower) {
        this.cardPower = cardPower;
    }

    public CardPower getCardPower() {
        return cardPower;
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
