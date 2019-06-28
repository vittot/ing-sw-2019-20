package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;
import game.model.Target;

import java.util.List;

public class AfterDamagePowerUpRequest implements ServerGameMessage {
    private List<CardPower> list;
    private List<Target> targets;

    public AfterDamagePowerUpRequest(List<CardPower> list, List<Target> targets) {
        this.list = list;
        this.targets = targets;
    }

    public List<CardPower> getList() {
        return list;
    }

    public List<Target> getTargets() {
        return targets;
    }

    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

}

