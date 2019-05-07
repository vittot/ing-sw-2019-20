package game.controller.commands.servercommands;
import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Action;

import java.util.ArrayList;
import java.util.List;

public class ChooseSingleActionRequest implements ServerMessage {
    public List<Action> actions = new ArrayList<>();
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
