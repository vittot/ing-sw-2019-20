package game.controller.commands.servercommands;

import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.model.Player;
import game.model.Target;

import java.util.List;

public class NotifyDamageResponse implements ServerMessage {
    public Player shooter;
    public List<Player> hit;
    public int damage;
    public int marks;

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
