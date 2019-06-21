package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Player;

public class UpdateMarks implements ServerMessage {

    Player p;

    public UpdateMarks(Player p) {
        this.p = p;
    }

    public Player getP() {
        return p;
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
