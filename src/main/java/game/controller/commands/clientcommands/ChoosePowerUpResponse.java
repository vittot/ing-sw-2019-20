package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.CardPower;
import game.model.Color;

public class ChoosePowerUpResponse implements ClientMessage {
    private CardPower cardPower;
    private Color ammoToPay;
    private boolean confirm;

    public ChoosePowerUpResponse(CardPower cardPower) {
        this.cardPower = cardPower;
        this.ammoToPay = Color.ANY;
        this.confirm = true;
    }

    public ChoosePowerUpResponse(CardPower cardPower, Color ammoToPay) {
        this.cardPower = cardPower;
        this.ammoToPay = ammoToPay;
        this.confirm = true;
    }

    public ChoosePowerUpResponse() {
        this.confirm = false;
    }

    public CardPower getCardPower() {
        return cardPower;
    }

    public Color getAmmoToPay() {
        return ammoToPay;
    }

    public boolean isConfirm() {
        return confirm;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) {
        return handler.handle(this);
    }
}
