package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;
import game.model.Game;
import game.model.GameMap;
import game.model.Kill;

import java.util.List;

public class NotifyGameStarted implements ServerMessage {

    private GameMap map;
    private CardPower [] powerups;

    public NotifyGameStarted(GameMap map, CardPower c1, CardPower c2) {
        this.map = map;
        this.powerups = new CardPower[2];
        this.powerups[0] = c1;
        this.powerups[1] = c2;

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
