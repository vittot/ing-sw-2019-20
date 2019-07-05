package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardWeapon;

import java.util.List;

/**
 * message to allow the selection of the weapon to use in the shoot action phase
 */
public class ChooseWeaponToShootRequest implements ServerGameMessage {

    /**
     * weapons available to shoot
     */
    private List<CardWeapon> myWeapons;

    /**
     * construct correct message
     * @param myWeapons
     */
    public ChooseWeaponToShootRequest(List<CardWeapon> myWeapons) {
        this.myWeapons = myWeapons;
    }

    /**
     * return weapons list
     * @return weapons
     */
    public List<CardWeapon> getMyWeapons() {
        return myWeapons;
    }

    /**
     * Handle the message
     * @param handler who handle the message
     */
    @Override
    public void handle(ServerGameMessageHandler handler)  {
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
