package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.model.CardWeapon;

import java.util.List;

/**
 * message to allow the selection of the weapon to grab
 */
public class ChooseWeaponToGrabRequest implements ServerGameMessage {

    /**
     * list of weapons available to grab
     */
    private List<CardWeapon> weapons;

    /**
     * construct correct message
     * @param weapons
     */
    public ChooseWeaponToGrabRequest(List<CardWeapon> weapons) {
        this.weapons = weapons;
    }

    /**
     * return weapons list
     * @return weapons
     */
    public List<CardWeapon> getWeapons() {
        return weapons;
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
