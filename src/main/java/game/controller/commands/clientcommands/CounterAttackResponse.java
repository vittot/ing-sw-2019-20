package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardPower;
import game.model.Player;

/**
 * Response if the player want to use counter attack
 */
public class CounterAttackResponse implements ClientGameMessage {
    /**
     * Card power to use
     */
    private CardPower cardPower;
    /**
     * PLlayer that shoot
     */
    private Player toShoot;
    /**
     * true if the player want to use it
     */
    private boolean confirm;

    /**
     * Constructor
     * @param cardPower Cad power to use
     * @param toShoot player that shoot
     */
    public CounterAttackResponse(CardPower cardPower, Player toShoot) {
        this.cardPower = cardPower;
        this.toShoot = toShoot;
        this.confirm = true;
    }

    /**
     * empty contrusctor for negative choice
     */
    public CounterAttackResponse() {
        this.confirm = false;
    }

    /**
     *
     * @return return the card power selected
     */
    public CardPower getCardPower() {
        return cardPower;
    }

    /**
     *
     * @return the shooter
     */
    public Player getToShoot() {
        return toShoot;
    }

    /**
     *
     * @return the player choice
     */
    public boolean isConfirm() {
        return confirm;
    }

    /**
     * Handle the message
     * @param handler who handle the message
     * @return the message from the handler
     */
    @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */
    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }
}
