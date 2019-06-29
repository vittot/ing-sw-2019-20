package game.controller.commands.clientcommands;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerGameMessage;

public class JoinWaitingRoomRequest implements ClientGameMessage {

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
    public ServerGameMessage handle(ClientGameMessageHandler handler) {
        return handler.handle(this);
    }

    @Override
    public void handle(ClientMessageHandler handler) {
        handler.handle(this);
    }


}
