package game.controller;

import game.controller.commands.ClientMessage;
import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.controller.commands.servercommands.*;
import game.model.*;
import game.model.exceptions.InsufficientAmmoException;
import game.model.exceptions.MapOutOfLimitException;
import game.model.exceptions.NoCardAmmoAvailableException;
import game.model.exceptions.NoCardWeaponSpaceException;

import java.io.IOException;
import java.util.List;

public class ClientController implements ServerMessageHandler {
    private final Client client;
    private Thread receiver;
    private View clientView;
    private List<Action> availableActions;

    public ClientController(Client client) {
        this.client = client;
        clientView = new ClientTextView(this);
    }

    public Client getClient() {
        return client;
    }

    public List<Action> getAvailableActions() {
        return availableActions;
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
                        try {
                            sm.handle(this);
                        } catch (MapOutOfLimitException e) {
                            e.printStackTrace();
                        }
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
     * Send a series of messages to server
     * @param messages
     */
    public void sendMessages(List<ClientMessage> messages)
    {
        for(ClientMessage m : messages)
            client.sendMessage(m);
    }

    /**
     * Update the ClientContext reloading the weapon and removing the power-up cards and ammo cards used by the player
     * @param serverMsg
     */
    @Override
    public void handle(CheckReloadResponse serverMsg) {
        List<CardWeapon> weapons = ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).getWeapons();
        for(CardWeapon cW : weapons){
            if(cW.equals(serverMsg.getWeapon())) {
                try {
                    cW.reloadWeapon(serverMsg.getPowerUps());
                    clientView.showReloadMessage(cW);
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
        /*List<Action> possibleAction = new ArrayList<>();
        for(Action ac : serverMsg.getActions() ){
            if(!possibleAction.contains(ac))
                possibleAction.add(ac);
        }*/
        availableActions = serverMsg.getActions();
        clientView.chooseStepActionPhase();
    }

    /**
     * Ask the player what square want to select
     * @param serverMsg
     */
    @Override
    public void handle(ChooseSquareRequest serverMsg) {
        clientView.chooseSquarePhase(serverMsg.getPossiblePositions());
    }

    /**
     * Ask the player what target want to select to apply his action
     * @param serverMsg
     */
    @Override
    public void handle(ChooseTargetRequest serverMsg) {
        clientView.chooseTargetPhase(serverMsg.getPossibleTargets());
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
        clientView.showRanking(serverMsg.getRanking());
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
            if(serverMsg.getwWaste() != null)
                instance.getMap().getSquare(serverMsg.getX(),serverMsg.getY()).getWeapons().add(serverMsg.getwWaste());
        } catch (MapOutOfLimitException e) {
            //TODO shouldnt append
        }
        clientView.grabWeaponNotification(serverMsg.getP(),serverMsg.getCw().getName(),serverMsg.getX(),serverMsg.getY());

    }

    /**
     *
     * @param serverMsg
     */
    @Override
    public void handle(NotifyMovement serverMsg) {
        try {
            Player toMove = ClientContext.get().getMap().getPlayerById(serverMsg.getId());
            GameMap map = ClientContext.get().getMap();
            map.getSquare(toMove.getPosition().getX(),toMove.getPosition().getY()).removePlayer(toMove);
            toMove.setPosition(map.getSquare(serverMsg.getX(),serverMsg.getY()));
            map.getSquare(serverMsg.getX(),serverMsg.getY()).addPlayer(toMove);
            clientView.notifyMovement(toMove.getId(),toMove.getPosition().getX(),toMove.getPosition().getY());
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
        clientView.powerUpUsageNotification(serverMsg.getId(),serverMsg.getCp().getEffect().getName(),serverMsg.getCp().getEffect().getDescription());
    }

    /**
     *
     * @param serverMsg
     */
    @Override
    public void handle(PickUpAmmoResponse serverMsg) {
        //notify set null the card-ammo on the square
        for(Color c : serverMsg.getColors())
            ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).addAmmo(c);
        if(serverMsg.getPowerups() != null)
            for(CardPower cp : serverMsg.getPowerups())
                ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).addCardPower(cp);
    }

    /**
     *
     * @param serverMsg
     */
    @Override
    public void handle(PickUpWeaponResponse serverMsg) {
        try {
            ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).pickUpWeapon(serverMsg.getCw(),serverMsg.getCwToWaste(),serverMsg.getCp());
        } catch (InsufficientAmmoException e) {
            clientView.insufficientAmmoNotification();
        } catch (NoCardWeaponSpaceException e) {
            clientView.invalidWeaponNotification();
        }
    }

    /**
     *
     * @param serverMsg
     */
    @Override
    public void handle(RespawnRequest serverMsg) {
        Player p = ClientContext.get().getPlayersInWaiting().stream().filter(pl -> pl.getId() == ClientContext.get().getMyID()).findFirst().orElse(null);
        if(p == null){
            p = ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID());
        }
        p.addCardPower(serverMsg.getcPU());
        clientView.choosePowerUpToRespawn(p.getCardPower());
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
        if(!serverMsg.getMessage().equals(""))
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
    public void handle(InvalidGrabPositionResponse serverMsg) {
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

        if(serverMsg.getId() != 0)
            ClientContext.get().setMyID(serverMsg.getId());
        ClientContext.get().setPlayersInWaiting(serverMsg.getPlayers());


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
        clientView.notifyTurnChanged(notifyTurnChanged.getCurrPlayerId());
    }

    /**
     *
     * @param notifyMarks
     */
    @Override
    public void handle(NotifyMarks notifyMarks) {
        clientView.notifyMarks(notifyMarks.getMarks(),notifyMarks.getHitId(),notifyMarks.getShooterId());
    }

    /**
     *
     * @param notifyGrabCardAmmo
     */
    @Override
    public void handle(NotifyGrabCardAmmo notifyGrabCardAmmo) throws MapOutOfLimitException {
        ClientContext.get().getMap().getSquare(notifyGrabCardAmmo.getX(),notifyGrabCardAmmo.getY()).setCardAmmo(null);
        if(notifyGrabCardAmmo.getpId() != ClientContext.get().getMyID())
            for(Color c : notifyGrabCardAmmo.getAmmos())
                ClientContext.get().getMap().getPlayerById(notifyGrabCardAmmo.getpId()).addAmmo(c);
        clientView.notifyGrabCardAmmo(notifyGrabCardAmmo.getpId());
    }

    /**
     *
     * @param notifyRespawn
     */
    @Override
    public void handle(NotifyRespawn notifyRespawn) {

        Player p = ClientContext.get().getPlayersInWaiting().stream().filter(pl -> pl.getId() == notifyRespawn.getpId()).findFirst().orElse(null);
        if(p == null){
            p = ClientContext.get().getMap().getPlayerById(notifyRespawn.getpId());
        }

        try {
            ClientContext.get().getMap().movePlayer(p,notifyRespawn.getX(),notifyRespawn.getY());
        } catch (MapOutOfLimitException e) {
            e.printStackTrace();
        }

        clientView.notifyRespawn(notifyRespawn.getpId());
    }

    @Override
    public void handle(AvailableMapsListResponse availableMapsListResponse) {
        clientView.showMapsPhase(availableMapsListResponse.getAvaiableMaps());
    }

    @Override
    public void handle(JoinWaitingRoomResponse joinWaitingRoomResponse) {
        ClientContext.get().setMyID(joinWaitingRoomResponse.getId());
        clientView.notifyCompletedOperation("You correctly joined the waiting room! Wait for other players...");
    }

    @Override
    public void handle(CreateWaitingRoomResponse createWaitingRoomResponse) {
        ClientContext.get().setMyID(createWaitingRoomResponse.getId());
        clientView.notifyCompletedOperation("Waiting room correctly created! \n>>Wait for other players...");
    }

    @Override
    public void handle(ChooseWeaponToGrabRequest chooseWeaponToGrabRequest){
        clientView.chooseWeaponToGrab(chooseWeaponToGrabRequest.getWeapons());
    }

    @Override
    public void handle(ReloadWeaponAsk reloadWeaponAsk) {
        clientView.reloadWeaponPhase(reloadWeaponAsk.getWeaponsToReload());
    }
}
