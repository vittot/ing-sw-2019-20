package game.controller;

import game.controller.commands.ServerMessageHandler;
import game.controller.commands.servercommands.*;
import game.model.CardWeapon;
import game.model.Player;
import game.model.Kill;
import game.model.exceptions.InsufficientAmmoException;
import game.model.exceptions.MapOutOfLimitException;
import game.model.exceptions.NoCardAmmoAvailableException;
import game.model.Action;
import game.model.exceptions.NoCardWeaponSpaceException;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class ClientController implements ServerMessageHandler {
    // reference to networking layer
    private final Client client;
    private Thread receiver;

    //TODO: add the view

    public ClientController(Client client) {
        this.client = client;
    }


    public void start() {

        receiver = new Thread(
                //receive messages from the Server
        );
        receiver.start();
    }


    public void run() throws IOException {
        //TODO: launch various phases on the view
        return;
    }

    // TODO: ------ ServerMessage handling

    /**
     * Update the ClientContext reloading the weapon and removing the power-up cards and ammo cars used by the player
     * @param serverMsg
     */
    @Override
    public void handle(CheckReloadResponse serverMsg) {
        List<CardWeapon> weapons = ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).getWeapons();
        for(CardWeapon cW : weapons){
            if(cW.equals(serverMsg.weapon)) {
                try {
                    cW.reloadWeapon(serverMsg.powerUps);
                    //have we to simplify it?????
                }
                catch(InsufficientAmmoException e){
                    //TODO exception case
                }
            }
        }
        return;
    }

    @Override
    public void handle(ChooseSingleActionRequest serverMsg) {
        List<Action> possibleAction = new ArrayList<>();
        for(Action ac : serverMsg.actions ){
            if(!possibleAction.contains(ac))
                possibleAction.add(ac);
        }
        //TODO view methods choose step action
        //chooseStepAction(possibleAction)
        return;
    }

    @Override
    public void handle(ChooseSquareRequest serverMsg) {
        // TODO view method to permit the player to choose one or some targets from the possibles (parameter)
        return;
    }

    @Override
    public void handle(ChooseTargetRequest serverMsg) {
        //TODO call view mwthods
        return;
    }

    @Override
    public void handle(ChooseTurnActionRequest serverMsg) {
        // TODO view method to permit the player to choose the TurnAction he wants to perform
        return;
    }

    @Override
    public void handle(InvalidTargetResponse serverMsg) {
        //TODO call view methods
        return;
    }

    @Override
    public void handle(InvalidWeaponResponse serverMsg) {
        // TODO view method
        return;

    }

    @Override
    public void handle(NotifyDamageResponse serverMsg) {
        for(Player p : serverMsg.hit) {
            ClientContext.get().getMap().getPlayersById(serverMsg.hit.getId()).addDamage(serverMsg.shooter, serverMsg.damage);
            ClientContext.get().getMap().getPlayersById(serverMsg.hit.getId()).adThisTurnMarks(serverMsg.shooter, serverMsg.marks);
        }
        return;

    }

    @Override
    public void handle(NotifyDeathResponse serverMsg) {
        ClientContext instance = ClientContext.get();
        instance.getKillboard().add(serverMsg.kill);
        //TODO Update death view methods(kill)
        return;

    }

    @Override
    public void handle(NotifyEndGameResponse serverMsg) {
        // TODO view method to show the player the final ranking of the game, the game is over
        return;
    }

    @Override
    public void handle(NotifyGrabResponse serverMsg) {
        ClientContext instance = ClientContext.get();
        try {
            instance.getMap().getSquare(serverMsg.x,serverMsg.y).setCardAmmo(null);
            instance.getMap().getSquare(serverMsg.x,serverMsg.y).getWeapons().remove(serverMsg.cw);
        } catch (MapOutOfLimitException e) {
            //TODO exc
        }
        //TODO view methods notify grab ammo/weapon
        return;
    }

    @Override
    public void handle(NotifyMovementResponse serverMsg) {
        try {
            ClientContext.get().getMap().getPlayerById(serverMsg.id).setPosition(ClientContext.get().getMap().getSquare(serverMsg.x, serverMsg.y));
        }
        catch(MapOutOfLimitException e){
            // TODO
        }
        return;
    }

    @Override
    public void handle(NotifyPowerUpUsageResponse serverMsg) {
        ClientContext instance = ClientContext.get();
        if(instance.getMyId() == serverMsg.id){
            instance.getMap().getPlayerById(serverMsg.id).getCardPower().remove(serverMsg.cp);
            //TODO (Myplayer, cardpower)
        }
        //TODO (Player, cardPower)
        return;
    }

    @Override
    public void handle(PickUpAmmoResponse serverMsg) {
        try {
            ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID().pickUpAmmo());
        }
        catch(NoCardAmmoAvailableException e){

        }
        return;
    }

    @Override
    public void handle(PickUpWeaponResponse serverMsg) {
        try {
            ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyId()).pickUpWeapon(serverMsg.cw,serverMsg.cwToWaste,serverMsg.cp);
        } catch (InsufficientAmmoException e) {
            //TODO call view
        } catch (NoCardWeaponSpaceException e) {
            //tOdO call view
        }
        //TODO call view methods
        return;
    }

    @Override
    public void handle(RespawnRequest serverMsg) {
        ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).getCardPower().add(serverMsg.cPU);
        // TODO view method that allows the player to choose a power-up card to discard to select the map color where respawn
        return;
    }

    @Override
    public void handle(InsufficientAmmoResponse serverMsg) {
        //TODO view methods
        return;

    }

    @Override
    public void handle(OperationCompletedResponse serverMsg) {
        return;

    }

    @Override
    public void handle (AfterDamagePowerUpRequest serverMsg){
        //TODO richiesta powerUp
        return;
    }
}
