package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;

public class CreateWaitingRoomRequest implements ClientMessage {

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
    public ServerMessage handle(ClientMessageHandler handler) {
        return handler.handle(this);
    }

    public int getMapId() {
        return mapId;
    }

    public int getNumWaitingPlayers() {
        return numWaitingPlayers;
    }
}
