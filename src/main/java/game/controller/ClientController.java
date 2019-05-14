package game.controller;

import game.controller.commands.ServerMessage;
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
    private final Client client;
    private Thread receiver;
    private View clientView;

    public ClientController(Client client) {
        this.client = client;
        clientView = new ClientTextView(this);
    }

    public Client getClient() {
        return client;
    }

    /**
     * Start the listener thread for ServerMessages
     */
    public void start() {

        receiver = new Thread(

                () -> {
                    ServerMessage sm;
                    do{
                        sm = client.receiveMessage();
                        sm.handle(this);
                    }while(sm != null);
                }
        );
        receiver.start();
    }


    public void run() throws IOException {
        clientView.setUserNamePhase();
        this.start();
        //TODO: launch various phases on the view

    }

    /**
     * Update the ClientContext reloading the weapon and removing the power-up cards and ammo cards used by the player
     * @param serverMsg
     */
    @Override
    public void handle(CheckReloadResponse serverMsg) {
        List<CardWeapon> weapons = ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).getWeapons();
        for(CardWeapon cW : weapons){
            if(cW.equals(serverMsg.weapon)) {
                try {
                    cW.reloadWeapon(serverMsg.powerUps);
                }
                catch(InsufficientAmmoException e){
                    clientView.insufficientAmmoNotification();
                }
            }
        }
    }

    /**
     * Ask the player which possible step, contained in the list of the possible move he can make, want to select
     * @param serverMsg
     */
    @Override
    public void handle(ChooseSingleActionRequest serverMsg) {
        List<Action> possibleAction = new ArrayList<>();
        for(Action ac : serverMsg.actions ){
            if(!possibleAction.contains(ac))
                possibleAction.add(ac);
        }
        clientView.chooseStepActionPhase(possibleAction);
    }

    /**
     * Ask the player what square want to select
     * @param serverMsg
     */
    @Override
    public void handle(ChooseSquareRequest serverMsg) {
        clientView.chooseSquarePhase(serverMsg.possiblePositions);
    }

    /**
     * Ask the player what target want to select to apply his action
     * @param serverMsg
     */
    @Override
    public void handle(ChooseTargetRequest serverMsg) {
        clientView.chooseTargetPhase(serverMsg.possibleTargets);
    }

    /**
     * Ask the player what action want to choose for this turn phase
     * @param serverMsg
     */
    @Override
    public void handle(ChooseTurnActionRequest serverMsg) {

        clientView.chooseTurnActionPhase();
    }

    /**
     * Notify the player that the targets chosen are invalid
     * @param serverMsg
     */
    @Override
    public void handle(InvalidTargetResponse serverMsg) {
        clientView.invalidTargetNotification();
    }

    /**
     * Notify the player the invalid weapon selection
     * @param serverMsg
     */
    @Override
    public void handle(InvalidWeaponResponse serverMsg) {
        clientView.invalidWeaponNotification();

    }

    /**
     * Notify the player the invalid action selection
     * @param serverMsg
     */
    @Override
    public void handle(InvalidActionResponse serverMsg) {
        clientView.invalidActionNotification();
    }

    /**
     * Notify the player that he doesn't have other new actions available
     * @param serverMsg
     */
    @Override
    public void handle(InsufficientNumberOfActionResponse serverMsg) {
        clientView.insufficientNumberOfActionNotification();
    }

    /**
     * Notify the player the invalid step selection
     * @param serverMsg
     */
    @Override
    public void handle(InvalidStepResponse serverMsg) {
        clientView.invalidStepNotification();
    }

    /**
     * Notify the player that he doesn't have enough space to grab new weapons
     * @param serverMsg
     */
    @Override
    public void handle(MaxNumberOfWeaponsResponse serverMsg) {
        clientView.maxNumberOfWeaponNotification();
    }

    @Override
    public void handle(NotifyDamageResponse serverMsg) {
        Player shooter = ClientContext.get().getMap().getPlayerById(serverMsg.getShooterId());
        ClientContext.get().getMap().getPlayerById(serverMsg.getHit()).addDamage(shooter, serverMsg.getDamage());
        clientView.damageNotification(serverMsg.getShooterId(),serverMsg.getDamage(),serverMsg.getHit());

    }

    @Override
    public void handle(NotifyDeathResponse serverMsg) {
        ClientContext instance = ClientContext.get();
        instance.getKillboard().add(serverMsg.getKill());
        clientView.notifyDeath(serverMsg.getKill());
        //TODO Update death view methods(kill), pass a kill is a problem (????????)
        return;

    }

    /**
     * Show the player the final results of the game
     * @param serverMsg
     */
    @Override
    public void handle(NotifyEndGameResponse serverMsg) {
        clientView.showRanking(serverMsg.ranking);
    }

    /**
     *
     * @param serverMsg
     */
    @Override
    public void handle(NotifyGrabWeapon serverMsg) {
        ClientContext instance = ClientContext.get();
        try {
            instance.getMap().getSquare(serverMsg.getX(),serverMsg.getY()).getWeapons().remove(serverMsg.getCw());
            if(serverMsg.getwWaste() == null)
                instance.getMap().getSquare(serverMsg.getX(),serverMsg.getY()).getWeapons().add(serverMsg.getwWaste());
        } catch (MapOutOfLimitException e) {
            //TODO shouldnt append
        }
        clientView.grabWeaponNotification(serverMsg.getP(),serverMsg.getX(),serverMsg.getY());

    }

    /**
     *
     * @param serverMsg
     */
    @Override
    public void handle(NotifyMovement serverMsg) {
        try {
            ClientContext.get().getMap().getPlayerById(serverMsg.getId()).setPosition(ClientContext.get().getMap().getSquare(serverMsg.getX(), serverMsg.getY()));
        }
        catch(MapOutOfLimitException e){
            // TODO shouldnt append
        }
    }

    /**
     *
     * @param serverMsg
     */
    @Override
    public void handle(NotifyPowerUpUsage serverMsg) {
        ClientContext instance = ClientContext.get();
        if(instance.getMyID() == serverMsg.getId()){
            instance.getMap().getPlayerById(serverMsg.getId()).getCardPower().remove(serverMsg.getCp());
        }
        clientView.powerUpUsageNotification(serverMsg.getId(),serverMsg.getCp().getName(),serverMsg.getCp().getDescription());
    }

    /**
     *
     * @param serverMsg
     */
    @Override
    public void handle(PickUpAmmoResponse serverMsg) {
        try {
            ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).pickUpAmmo();
        }
        catch(NoCardAmmoAvailableException e){
            clientView.insufficientAmmoNotification();
        }
    }

    /**
     *
     * @param serverMsg
     */
    @Override
    public void handle(PickUpWeaponResponse serverMsg) {
        try {
            ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).pickUpWeapon(serverMsg.cw,serverMsg.cwToWaste,serverMsg.cp);
        } catch (InsufficientAmmoException e) {
            clientView.insufficientAmmoNotification();
        } catch (NoCardWeaponSpaceException e) {
            clientView.invalidWeaponNotification();
        }
        clientView.notifyWeaponGrab(serverMsg.cw.getName());
    }

    /**
     *
     * @param serverMsg
     */
    @Override
    public void handle(RespawnRequest serverMsg) {
        ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).getCardPower().add(serverMsg.getcPU());
        clientView.choosePowerUpToRespawn(ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).getCardPower());
    }

    /**
     *
     * @param serverMsg
     */
    @Override
    public void handle(InsufficientAmmoResponse serverMsg) {
        clientView.insufficientAmmoNotification();
    }

    /**
     *
     * @param serverMsg
     */
    @Override
    public void handle(OperationCompletedResponse serverMsg) {
        clientView.notifyCompletedOperation(serverMsg.getMessage());
    }

    /**
     *
     * @param serverMsg
     */
    @Override
    public void handle(InvalidPowerUpResponse serverMsg) {
        clientView.notifyInvalidPowerUP();
    }

    /**
     *
     * @param serverMsg
     */
    @Override
    public void handle(InvalidGrabPositionRsponse serverMsg) {
        clientView.notifyInvalidGrabPosition();
    }

    /**
     *
     * @param serverMsg
     */
    @Override
    public void handle (AfterDamagePowerUpRequest serverMsg){
        clientView.choosePowerUpToUse(ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).getCardPower());
    }

    /**
     *
     * @param serverMsg
     */
    @Override
    public void handle(NotifyGameStarted serverMsg) {

        ClientContext.get().setMap(serverMsg.getMap());
        clientView.notifyStart();
    }

    /**
     *
     * @param waitingRoomsListResponse
     */
    @Override
    public void handle(WaitingRoomsListResponse waitingRoomsListResponse) {
        clientView.chooseRoomPhase(waitingRoomsListResponse.getAvaiableWaitingRooms());
    }

    /**
     *
     * @param invalidMessageResponse
     */
    @Override
    public void handle(InvalidMessageResponse invalidMessageResponse) {
        clientView.notifyInvalidMessage();
    }

    /**
     *
     * @param notifyTurnChanged
     */
    @Override
    public void handle(NotifyTurnChanged notifyTurnChanged) {
        clientView.notifyTurnChanged();
    }

    /**
     *
     * @param notifyMarks
     */
    @Override
    public void handle(NotifyMarks notifyMarks) {
        clientView.notifyMarks();
    }

    /**
     *
     * @param notifyGrabAmmo
     */
    @Override
    public void handle(NotifyGrabAmmo notifyGrabAmmo) {
        clientView.notifyGrabAmmo();
    }

    /**
     *
     * @param notifyRespawn
     */
    @Override
    public void handle(NotifyRespawn notifyRespawn) {
        clientView.notifyRespawn();
    }

    @Override
    public void handle(AvailableMapsListResponse availableMapsListResponse) {
        clientView.showMapsPhase(availableMapsListResponse.getAvaiableMaps());
    }
}
