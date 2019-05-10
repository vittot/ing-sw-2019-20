package game.controller;

import game.controller.commands.ServerMessageHandler;
import game.controller.commands.servercommands.*;
import game.model.CardWeapon;
import game.model.Player;
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
    //private View clientView;
    private ClientTextView clientView;
    //TODO: add the view

    public ClientController(Client client) {
        this.client = client;
        clientView = new ClientTextView(this);
    }


    public void start() {

        receiver = new Thread(
                //receive messages from the Server
        );
        receiver.start();
    }


    public void run() throws IOException {
        clientView.setUserNamePhase();
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
        clientView.chooseStepActionPhase(possibleAction);
        return;
    }

    @Override
    public void handle(ChooseSquareRequest serverMsg) {
        clientView.chooseSquarePhase(serverMsg.possiblePositions);
        return;
    }

    @Override
    public void handle(ChooseTargetRequest serverMsg) {
        clientView.chooseTargetPhase(serverMsg.possibleTargets);
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
    public void handle(InvalidActionResponse serverMsg) {

    }

    @Override
    public void handle(InvalidNumberOfActionResponse serverMsg) {

    }

    @Override
    public void handle(InvalidStepResponse serverMsg) {

    }

    @Override
    public void handle(MaxNumberOfWeaponsResponse serverMsg) {

    }

    @Override
    public void handle(InvalidDeathResponse serverMsg) {

    }

    @Override
    public void handle(NotifyDamageResponse serverMsg) {
        Player shooter = ClientContext.get().getMap().getPlayerById(serverMsg.getShooterId());
            ClientContext.get().getMap().getPlayerById(serverMsg.getHit()).addDamage(shooter, serverMsg.getDamage());

    }

    @Override
    public void handle(NotifyDeathResponse serverMsg) {
        ClientContext instance = ClientContext.get();
        instance.getKillboard().add(serverMsg.getKill());
        //TODO Update death view methods(kill)
        return;

    }

    @Override
    public void handle(NotifyEndGameResponse serverMsg) {
        // TODO view method to show the player the final ranking of the game, the game is over
        return;
    }

    @Override
    public void handle(NotifyGrabWeapon serverMsg) {
        ClientContext instance = ClientContext.get();
        try {
            instance.getMap().getSquare(serverMsg.getX(),serverMsg.getY()).setCardAmmo(null);
            instance.getMap().getSquare(serverMsg.getX(),serverMsg.getY()).getWeapons().remove(serverMsg.getCw());
        } catch (MapOutOfLimitException e) {
            //TODO exc
        }
        //TODO view methods notify grab ammo/weapon
        return;
    }

    @Override
    public void handle(NotifyMovement serverMsg) {
        try {
            ClientContext.get().getMap().getPlayerById(serverMsg.getId()).setPosition(ClientContext.get().getMap().getSquare(serverMsg.getX(), serverMsg.getY()));
        }
        catch(MapOutOfLimitException e){
            // TODO
        }
        return;
    }

    @Override
    public void handle(NotifyPowerUpUsage serverMsg) {
        ClientContext instance = ClientContext.get();
        if(instance.getMyID() == serverMsg.getId()){
            instance.getMap().getPlayerById(serverMsg.getId()).getCardPower().remove(serverMsg.getCp());
            //TODO (Myplayer, cardpower)
        }
        //TODO (Player, cardPower)
        return;
    }

    @Override
    public void handle(PickUpAmmoResponse serverMsg) {
        try {
            ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).pickUpAmmo();
        }
        catch(NoCardAmmoAvailableException e){

        }
        return;
    }

    @Override
    public void handle(PickUpWeaponResponse serverMsg) {
        try {
            ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).pickUpWeapon(serverMsg.cw,serverMsg.cwToWaste,serverMsg.cp);
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
        ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).getCardPower().add(serverMsg.getcPU());
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
    public void handle(InvalidPowerUpResponse serverMsg) {

    }

    @Override
    public void handle(InvalidGrabPositionRsponse serverMsg) {

    }

    @Override
    public void handle (AfterDamagePowerUpRequest serverMsg){
        //TODO richiesta powerUp
        return;
    }

    @Override
    public void handle(NotifyGameStarted serverMsg) {
        //TODO
    }


    @Override
    public void handle(WaitingRoomsListResponse waitingRoomsListResponse) {
        ///TODO
    }

    @Override
    public void handle(InvalidMessageResponse invalidMessageResponse) {

    }

    @Override
    public void handle(NotifyTurnChanged notifyTurnChanged) {
        //TODO
    }

    @Override
    public void handle(NotifyMarks notifyMarks) {

    }

    @Override
    public void handle(NotifyGrabAmmo notifyGrabAmmo) {

    }

    @Override
    public void handle(NotifyRespawn notifyRespawn) {

    }
}
