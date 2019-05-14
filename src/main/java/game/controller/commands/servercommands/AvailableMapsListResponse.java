package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.GameMap;

import java.util.List;

public class AvailableMapsListResponse implements ServerMessage {

    private List<GameMap> avaiableMaps;

    public AvailableMapsListResponse(List<GameMap> avaiableMaps) {
        this.avaiableMaps = avaiableMaps;
    }

    public List<GameMap> getAvaiableMaps() {
        return avaiableMaps;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
