package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Map;

import java.util.List;

public class AvailableMapsListResponse implements ServerMessage {

    private List<Map> avaiableMaps;

    public AvailableMapsListResponse(List<Map> avaiableMaps) {
        this.avaiableMaps = avaiableMaps;
    }

    public List<Map> getAvaiableMaps() {
        return avaiableMaps;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
