package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;

public class NotifyPowerUpUsage implements ServerGameMessage {
    private int id;
    private CardPower cp;

    public NotifyPowerUpUsage(int id, CardPower cp) {
        this.id = id;
        this.cp = cp;
    }

    public int getId() {
        return id;
    }

    public CardPower getCp() {
        return cp;
    }

    /**
     * Handle the message
     * @param handler who handle the messag
     * @param handler
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the messag
     * @param handler
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}