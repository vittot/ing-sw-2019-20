package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.GameMap;

import java.util.List;

/**
 * message containing the available maps to choose from to start a new game
 */
public class AvailableMapsListResponse implements ServerGameMessage {

    /**
     * list of available maps
     */
    private List<GameMap> avaiableMaps;

    /**
     * construct the message
     * @param avaiableMaps
     */
    public AvailableMapsListResponse(List<GameMap> avaiableMaps) {
        this.avaiableMaps = avaiableMaps;
    }

    /**
     * return the list of maps
     * @return availableMaps
     */
    public List<GameMap> getAvaiableMaps() {
        return avaiableMaps;
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
