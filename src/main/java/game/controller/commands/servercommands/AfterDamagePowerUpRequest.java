package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardPower;
import game.model.Target;

import java.util.List;

/**
 * message to ask the client if he want to use a targeting scope card to apply an additional damage
 */
public class AfterDamagePowerUpRequest implements ServerGameMessage {
    private List<CardPower> list; /** list of targeting scope card to choose from */
    private List<Target> targets; /** list of possible target to dealt a damage */

    /**
     * construct a message with correct parameters
     * @param list
     * @param targets
     */
    public AfterDamagePowerUpRequest(List<CardPower> list, List<Target> targets) {
        this.list = list;
        this.targets = targets;
    }

    /**
     * return list of targeting scope
     * @return list
     */
    public List<CardPower> getList() {
        return list;
    }

    /**
     * return the possible targets to shoot
     * @return targets
     */
    public List<Target> getTargets() {
        return targets;
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }

}

