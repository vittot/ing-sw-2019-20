package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;

public class CreateWaitingRoomRequest implements ClientGameMessage {

    private int mapId;
    private int numWaitingPlayers;
    private String creatorNicknme;

    public CreateWaitingRoomRequest(int mapId, int numWaitingPlayers, String creatorNicknme) {
        this.mapId = mapId;
        this.numWaitingPlayers = numWaitingPlayers;
        this.creatorNicknme = creatorNicknme;
    }

    public String getCreatorNicknme() {
        return creatorNicknme;
    }

    @Override
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }

    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }

    public int getMapId() {
        return mapId;
    }

    public int getNumWaitingPlayers() {
        return numWaitingPlayers;
    }
}
