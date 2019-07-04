package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;

/**
 * Request from client fo creating a waitin room
 */
public class CreateWaitingRoomRequest implements ClientGameMessage {
    /**
     * Number of map id
     */
    private int mapId;
    /**
     * Number of player in waiting
     */
    private int numWaitingPlayers;
    /**
     * Nickname of the Creator
     */
    private String creatorNicknme;

    /**
     * Constructor
     * @param mapId id map
     * @param creatorNicknme nickname
     */
    public CreateWaitingRoomRequest(int mapId, String creatorNicknme) {
        this.mapId = mapId;
        this.creatorNicknme = creatorNicknme;
    }

    /**
     *
     * @return the nickname of the creator
     */
    public String getCreatorNicknme() {
        return creatorNicknme;
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
     *
     * @return map id
     */
    public int getMapId() {
        return mapId;
    }

    /**
     *
     * @return the number of playiner in waiting
     */
    public int getNumWaitingPlayers() {
        return numWaitingPlayers;
    }
}
