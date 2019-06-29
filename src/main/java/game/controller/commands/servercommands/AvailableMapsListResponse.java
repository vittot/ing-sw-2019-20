package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.GameMap;

import java.util.List;

public class AvailableMapsListResponse implements ServerGameMessage {

    private List<GameMap> avaiableMaps;

    public AvailableMapsListResponse(List<GameMap> avaiableMaps) {
        this.avaiableMaps = avaiableMaps;
    }

    public List<GameMap> getAvaiableMaps() {
        return avaiableMaps;
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
