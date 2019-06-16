package game.controller;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.*;
import game.model.exceptions.InsufficientAmmoException;

import java.beans.Transient;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.SortedMap;

public class RMIClientHandler extends UnicastRemoteObject implements ClientHandler {
    private transient ServerController controller;
    private transient RMIClient client;

    public RMIClientHandler() throws RemoteException {
        super();
        this.controller = new ServerController(this);
    }

    @Override
    public void sendMessage(ServerMessage msg) {
        client.receiveMessage(msg);
    }


    public void receiveMessage(ClientMessage cmsg)
    {
        cmsg.handle(controller);
    }

    @Override
    public void onChangeTurn(Player p) {

    }

    @Override
    public void onGameEnd(SortedMap<Player, Integer> gameRanking) {

    }

    @Override
    public void onDamage(Player damaged, Player attacker, int damage) {

    }

    @Override
    public void onMarks(Player marked, Player marker, int marks) {

    }

    @Override
    public void onDeath(Player dead) {

    }

    @Override
    public void onDeath(Kill kill) {

    }

    @Override
    public void onGrabWeapon(Player p, CardWeapon cw) {

    }

    @Override
    public void onGrabCardAmmo(Player p, List<Color> ammo) {

    }

    @Override
    public void onMove(Player p) {

    }

    @Override
    public void onRespawn(Player p) {

    }

    @Override
    public void onPowerUpUse(Player p, CardPower c) {

    }

    @Override
    public void onPlayerSuspend(Player p) {

    }

    @Override
    public void onPlayerRejoined(Player player) {

    }

    @Override
    public void onReplaceWeapon(CardWeapon cw, Square s) {

    }

    @Override
    public void onReplaceAmmo(CardAmmo ca, Square s) {

    }

    @Override
    public void onPlayerUpdateMarks(Player player) {

    }


    //TODO: methods analogous to the socket Requests
}
