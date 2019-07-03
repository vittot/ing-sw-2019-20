package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardPower;
import game.model.Color;

import java.util.List;

public class ChoosePowerUpResponse implements ClientGameMessage {
    private CardPower cardPower;
    private Color ammoToPay;
    private boolean confirm;
    private List<CardPower> cpToPay;

    public ChoosePowerUpResponse(CardPower cardPower) {
        this.cardPower = cardPower;
        this.ammoToPay = Color.ANY;
        this.confirm = true;
    }

    public ChoosePowerUpResponse(CardPower cardPower, Color ammoToPay, List<CardPower> cpToPay) {
        this.cardPower = cardPower;
        this.ammoToPay = ammoToPay;
        this.cpToPay = cpToPay;
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

    public List<CardPower> getCpToPay() {
        return cpToPay;
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
