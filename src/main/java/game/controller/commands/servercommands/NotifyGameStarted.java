package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.*;

import java.util.List;

public class NotifyGameStarted implements ServerMessage {

    private GameMap map;
    private CardPower [] powerups;
    private Player p;

    public NotifyGameStarted(Player p, GameMap map, CardPower c1, CardPower c2) {
        this.map = map;
        this.powerups = new CardPower[2];
        this.powerups[0] = c1;
        this.powerups[1] = c2;
        this.p = p;

    }

    public Player getP() {
        return p;
    }

    public CardPower[] getPowerups() {
        return powerups;
    }

    public GameMap getMap() {
        return map;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
