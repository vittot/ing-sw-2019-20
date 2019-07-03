package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardPower;
import game.model.Color;

import java.util.List;

/**
 * Class used to response for target scope request
 */
public class ChoosePowerUpResponse implements ClientGameMessage {
    /**
     * Card power selected
     */
    private CardPower cardPower;
    /**
     * Ammo used for payment
     */
    private Color ammoToPay;
    /**
     * Bool used to say if the client want to use some power up
     */
    private boolean confirm;
    /**
    *list of card power used for payment
     */
    private List<CardPower> cpToPay;

    /**
     * Constructor
     * @param cardPower card power selected
     */
    public ChoosePowerUpResponse(CardPower cardPower) {
        this.cardPower = cardPower;
        this.ammoToPay = Color.ANY;
        this.confirm = true;
    }

    /**
     * Constructor
     * @param cardPower Card power to use
     * @param ammoToPay ammo for payment
     * @param cpToPay  Card power for payment
     */
    public ChoosePowerUpResponse(CardPower cardPower, Color ammoToPay, List<CardPower> cpToPay) {
        this.cardPower = cardPower;
        this.ammoToPay = ammoToPay;
        this.cpToPay = cpToPay;
        this.confirm = true;
    }

    /**
     * Called when the player doesn't want to use some
     * set the confirm to false
     */
    public ChoosePowerUpResponse() {
        this.confirm = false;
    }

    /**
     * Return the card power
     * @return card power
     */
    public CardPower getCardPower() {
        return cardPower;
    }

    /**
     * Return the ammo for payment
     * @return ammo for paymert
     */
    public Color getAmmoToPay() {
        return ammoToPay;
    }

    /**
     *
     * @return the cp to pay
     */
    public List<CardPower> getCpToPay() {
        return cpToPay;
    }

    /**
     * return the confirm
     * @return
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
