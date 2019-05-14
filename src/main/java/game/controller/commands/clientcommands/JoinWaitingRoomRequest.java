package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;

public class JoinWaitingRoomRequest implements ClientMessage {

    private int roomId;
    private String nickName;

    public JoinWaitingRoomRequest(int roomId, String nickName) {
        this.roomId = roomId;
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
    }

    public int getRoomId() {
        return roomId;
    }

    @Override
    public ServerMessage handle(ClientMessageHandler handler) {
        return handler.handle(this);
    }


}
