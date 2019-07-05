package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;

/**
 * notify the correct usage of a power-up card and his effect completion
 */
public class ChoosePowerUpUsed implements ServerGameMessage {

    /**
     * power-up used by the client and to remove
     */
    private CardPower cardPower;

    /**
     * construct the message
     * @param cardPower
     */
    public ChoosePowerUpUsed(CardPower cardPower) {
        this.cardPower = cardPower;
    }

    /**
     * return the power-up
     * @return cardPower
     */
    public CardPower getCardPower() {
        return cardPower;
    }

    /**
     * Handle the message
     * @param handler who handle the message
     * @return the message from the handler
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the message
     * @return the message from the handler
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
