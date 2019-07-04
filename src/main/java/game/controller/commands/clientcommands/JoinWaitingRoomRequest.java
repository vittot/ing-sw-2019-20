package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;

/**
 * request of join the waiting room
 */
public class JoinWaitingRoomRequest implements ClientGameMessage {
    /**
     * Id of the waiting room selected
     */
    private int roomId;
    /**
     * Nickname of the player that want oto join
     */
    private String nickName;

    /**
     * Constructor
     * @param roomId int id
     * @param nickName String nickname
     */
    public JoinWaitingRoomRequest(int roomId, String nickName) {
        this.roomId = roomId;
        this.nickName = nickName;
    }

    /**
     *
     * @return the nickname
     */
    public String getNickName() {
        return nickName;
    }

    /**
     *
     * @return the id of the room
     */
    public int getRoomId() {
        return roomId;
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


}
