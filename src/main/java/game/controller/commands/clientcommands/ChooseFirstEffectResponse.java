package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;
import game.model.CardPower;

import java.util.List;

/**
 * Class used to response with effect to do and card power to pay
 */
public class ChooseFirstEffectResponse implements ClientGameMessage {
    /**
     * List of cardd power to pay
     */
    private List<CardPower> toUse;
    /**
     * index of effect to do
     */
    private int n;

    /**
     * Constructor
     * @param n identify the effect the client want to do
     * @param toUse list of cadr power to pay
     */
    public ChooseFirstEffectResponse(int n, List<CardPower> toUse) {
        this.toUse = toUse;
        this.n = n;
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

    /**
     * get n
     * @return the n
     */
    public int getN() {
        return n;
    }

    /**
     * return the List of card power
     * @return list of card power
     */
    public List<CardPower> getToUse() {
        return toUse;
    }
}
