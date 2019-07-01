package game.controller;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;
import game.controller.commands.clientcommands.GetAvailableMapsRequest;
import game.controller.commands.clientcommands.GrabActionRequest;
import game.controller.commands.servercommands.*;
import game.model.*;
import game.model.exceptions.InsufficientAmmoException;
import game.model.exceptions.MapOutOfLimitException;
import game.model.exceptions.NoCardWeaponSpaceException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ClientController implements ServerGameMessageHandler {
    private final Client client;
    private View clientView;
    private List<Action> availableActions;
    private ClientState state;
    private boolean gameStarted;
    private boolean gameEnded;

    public ClientController(Client client, View view) {
        this.client = client;
        this.gameStarted = false;
        this.clientView = view;
        this.clientView.setController(this);
        this.state = ClientState.WAITING_START;
        this.gameEnded = false;
    }

    public Client getClient() {
        return client;
    }

    public ClientState getState() {
        return state;
    }

    public void setState(ClientState state) {
        this.state = state;
    }

    public List<Action> getAvailableActions() {
        return availableActions;
    }

    /**
     * Start the listener thread for ServerMessages
     */
    private void start() {
        client.startListening(this);
    }



    public void run() {
        this.start();
        clientView.setUserNamePhase();


    }

    /**
     * Send a series of messages to server
     * @param messages
     */
    public void sendMessages(List<ClientGameMessage> messages)
    {
        for(ClientGameMessage m : messages)
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
        ClientContext.get().setCurrentEffect(serverMsg.getCurrSimpleEffect());
        clientView.chooseTargetPhase(serverMsg.getPossibleTargets());
    }

    /**
     * Ask the player what action want to choose for this turn phase
     * @param serverMsg
     */
    @Override
    public void handle(ChooseTurnActionRequest serverMsg) {

        this.state = ClientState.WAITING_ACTION;
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
        Player hitten = ClientContext.get().getMap().getPlayerById(serverMsg.getHit());
        List<CardPower> counterattack = null;
        for(int i = 0; i < serverMsg.getDamage(); i++)
            hitten.getDamage().add(shooter.getColor());
        for(int i = 0; i < serverMsg.getMarksToRemove(); i++)
            hitten.getMark().remove(shooter.getColor());
        clientView.damageNotification(serverMsg.getShooterId(),serverMsg.getDamage(),serverMsg.getHit());
        if(hitten.getId() == ClientContext.get().getMyID()) {
            counterattack = new ArrayList<>(ClientContext.get().getMyPlayer().getCardPower());
            for (CardPower cp : ClientContext.get().getMyPlayer().getCardPower())
                if (!cp.getName().equalsIgnoreCase("Tagback Grenade"))
                    counterattack.remove(cp);
            if (!counterattack.isEmpty())
                clientView.chooseCounterAttack(counterattack, shooter);
        }
    }

    @Override
    public void handle(NotifyDeathResponse serverMsg) {
        ClientContext instance = ClientContext.get();
        Player killer = ClientContext.get().getMap().getPlayerById(serverMsg.getIdKiller());
        Player victim = ClientContext.get().getMap().getPlayerById(serverMsg.getIdVictim());
        Kill kill = new Kill(killer,victim,serverMsg.isRage());
        instance.getKillboard().add(kill);
        clientView.notifyDeath(kill);
        //TODO Update death view methods(kill), pass a kill is a problem (????????)
        return;

    }

    /**
     * Show the player the final results of the game
     * @param serverMsg
     */
    @Override
    public void handle(NotifyEndGame serverMsg) {
        if(this.state != ClientState.GAME_END && this.state != ClientState.WAITING_START)
        {
            this.state = ClientState.GAME_END;
            clientView.showRanking(serverMsg.getRanking());
        }
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
        if(!serverMsg.getPowerups().isEmpty())
            for(CardPower cp : serverMsg.getPowerups())
                ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).addCardPower(cp);
        //TODO: show on the view
        clientView.notifyCompletedOperation("");
    }

    /**
     *
     * @param serverMsg
     */
    @Override
    public void handle(PickUpWeaponResponse serverMsg) {
        Player me = ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID());
        try {
            me.pickUpWeapon(serverMsg.getCw(),serverMsg.getCwToWaste(),serverMsg.getCp());
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
        //System.out.println("SONO NELL'HANDLE DEL RESPAWN");

        Player p = ClientContext.get().getPlayersInWaiting().stream().filter(pl -> pl.getId() == ClientContext.get().getMyID()).findFirst().orElse(ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()));
        //System.out.println("HO PRESO IL MIO PLAYER");
        p.addCardPower(serverMsg.getcPU());
        //System.out.println("GLI HO DATO LA POWER UP");
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
        if(this.state == ClientState.WAITING_ACTION)
            clientView.chooseTurnActionPhase();
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
    public void handle(AfterDamagePowerUpRequest serverMsg){
        clientView.choosePowerUpToUse(ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).getCardPower());
    }

    /**
     *
     * @param serverMsg
     */
    @Override
    public void handle(NotifyGameStarted serverMsg) {

        this.gameStarted = true;
        this.state = ClientState.WAITING_SPAWN;
        ClientContext.get().setMap(serverMsg.getMap());

        if(serverMsg.getId() != 0)
            ClientContext.get().setMyID(serverMsg.getId());
        ClientContext.get().setPlayersInWaiting(serverMsg.getPlayers());
        ClientContext.get().setKillboard(serverMsg.getKillBoard());
        if(ClientContext.get().getMyPlayer().getCardPower() == null)
        {
            System.out.println("NON HO LE MIE CARD POWER =( !");
            for(Player p : serverMsg.getPlayers())
            {
                System.out.println("Player " + p.getId() + " powercard= " + p.getCardPower());
            }
        }
        //System.out.println("GAME STARTED FINISHED");
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
        clientView.notifyTurnChanged(notifyTurnChanged.getCurrPlayerId());
    }

    /**
     *
     * @param notifyMarks
     */
    @Override
    public void handle(NotifyMarks notifyMarks) {
        Player shooter = ClientContext.get().getMap().getPlayerById(notifyMarks.getShooterId());
        ClientContext.get().getMap().getPlayerById(notifyMarks.getHitId()).addThisTurnMarks(shooter,notifyMarks.getMarks());
        clientView.notifyMarks(notifyMarks.getMarks(),notifyMarks.getHitId(),notifyMarks.getShooterId());
    }

    /**
     *
     * @param notifyGrabCardAmmo
     */
    @Override
    public void handle(NotifyGrabCardAmmo notifyGrabCardAmmo) {
        try {
            ClientContext.get().getMap().getSquare(notifyGrabCardAmmo.getX(), notifyGrabCardAmmo.getY()).setCardAmmo(null);
        }catch(MapOutOfLimitException e)
        {
            clientView.notifyCompletedOperation("ERROR: receive a notification about card ammo grab outside of the map!");
            return;
        }
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
        if(p.getId() == ClientContext.get().getMyID())
            this.state = ClientState.WAITING_ACTION;
        ClientContext.get().getPlayersInWaiting().remove(p);
        clientView.notifyRespawn(notifyRespawn.getpId());
    }

    @Override
    public void handle(AvailableMapsListResponse availableMapsListResponse) {
        clientView.showMapsPhase(availableMapsListResponse.getAvaiableMaps());
    }

    @Override
    public void handle(JoinWaitingRoomResponse joinWaitingRoomResponse) {
        ClientContext.get().setMyID(joinWaitingRoomResponse.getId());
        ClientContext.get().setPlayersInWaiting(new ArrayList(joinWaitingRoomResponse.getWaitingRoom().getPlayers()));
        clientView.notifyCompletedOperation("You correctly joined the waiting room! Wait for other players...");
    }

    @Override
    public void handle(CreateWaitingRoomResponse createWaitingRoomResponse) {
        ClientContext.get().setMyID(createWaitingRoomResponse.getId());
        ClientContext.get().setPlayersInWaiting(new ArrayList<>());
        clientView.notifyCompletedOperation("Waiting room correctly created! \n>>Wait for other players...");
        clientView.waitStart();
    }

    @Override
    public void handle(ChooseWeaponToGrabRequest chooseWeaponToGrabRequest){
        state = ClientState.WAITING_GRAB_WEAPON;
        clientView.chooseWeaponToGrab(chooseWeaponToGrabRequest.getWeapons());
    }

    @Override
    public void handle(ReloadWeaponAsk reloadWeaponAsk) {
        clientView.reloadWeaponPhase(reloadWeaponAsk.getWeaponsToReload());
    }

    @Override
    public void handle(NotifyPlayerSuspend notifyPlayerSuspend) {
        Player p = ClientContext.get().getPlayersInWaiting().stream().filter(pl -> pl.getId() == notifyPlayerSuspend.getpId()).findFirst().orElse(ClientContext.get().getMap().getPlayerById(notifyPlayerSuspend.getpId()));
        p.setSuspended(true);
        clientView.notifyPlayerSuspended(p);
    }

    /**
     * Notify time out and ask to rejoin the game
     * @param timeOutNotify
     */
    @Override
    public void handle(TimeOutNotify timeOutNotify) {
        if(state != ClientState.TIMED_OUT)
        {
            state = ClientState.TIMED_OUT;
            clientView.timeOutPhase();
        }

    }

    @Override
    public void handle(UserAlreadyLoggedResponse userAlreadyLoggedResponse) {
        clientView.alreadyLoggedPhase();
    }

    @Override
    public void handle(UserLoggedResponse userLoggedResponse) {
        clientView.loginCompletedPhase();
    }

    @Override
    public void handle(RejoinGameRequest rejoinGameRequest) {
        clientView.rejoinGamePhase(rejoinGameRequest.getOtherPlayerNames());
    }

    @Override
    public void handle(NotifyPlayerRejoin notifyPlayerRejoin) {
        Player p = ClientContext.get().getPlayersInWaiting().stream().filter(pl -> pl.getId() == notifyPlayerRejoin.getPlayerId()).findFirst().orElse(ClientContext.get().getMap().getPlayerById(notifyPlayerRejoin.getPlayerId()));
        if(p!=null)
        {
            p.setSuspended(false);
            clientView.notifyPlayerRejoin(p);
        }

    }

    @Override
    public void handle(RejoinGameConfirm rejoinGameConfirm) {
        ClientContext.get().setMap(rejoinGameConfirm.getMap());
        if(rejoinGameConfirm.getId() != 0)
            ClientContext.get().setMyID(rejoinGameConfirm.getId());
        ClientContext.get().setPlayersInWaiting(rejoinGameConfirm.getPlayers());
        for(Player p : rejoinGameConfirm.getPlayers())
            if(p.getPosition() != null) {
                try {
                    ClientContext.get().getMap().getSquare(p.getPosition().getX(),p.getPosition().getY()).addPlayer(p);
                } catch (MapOutOfLimitException e) {

                }
            }
        clientView.rejoinGameConfirm();
    }

    @Override
    public void handle(ChooseWeaponToShootRequest chooseWeaponToShootRequest) {
        state = ClientState.WAITING_SHOOT;
        clientView.chooseWeaponToShoot(chooseWeaponToShootRequest.getMyWeapons());
    }

    @Override
    public void handle(ChooseFirstEffectRequest chooseFirstEffectRequest) {
        clientView.chooseFirstEffect(chooseFirstEffectRequest.getBaseEff(),chooseFirstEffectRequest.getAltEff());
    }

    @Override
    public void handle(BeforeBaseRequest beforeBaseRequest) {
        clientView.usePlusBeforeBase(beforeBaseRequest.getPlusEff());
    }

    @Override
    public void handle(UsePlusEffectRequest usePlusEffectRequest) {
        clientView.choosePlusEffect(usePlusEffectRequest.getPlusEffects());
    }

    @Override
    public void handle(UsePlusByOrderRequest usePlusByOrderRequest) {
        clientView.usePlusInOrder(usePlusByOrderRequest.getPlusEffects());
    }

    @Override
    public void handle(ShootActionResponse shootActionResponse) {
        CardWeapon currWeapon = null;
        Player me = ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID());
        for(CardWeapon cw : me.getWeapons()) {
            if (cw.equals(shootActionResponse.getSelectedWeapon()))
                currWeapon = cw;
        }
        if(currWeapon != null) {
            currWeapon.setLoaded(false);
        }
    }

    @Override
    public void handle(RemoveSpawnPowerUp removeSpawnPowerUp) {
        Player me = ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID());
        if(me.getCardPower().contains(removeSpawnPowerUp.getPowerup()))
            me.getCardPower().remove(removeSpawnPowerUp.getPowerup());
    }

    @Override
    public void handle(NotifyWeaponRefill notifyWeaponRefill){
        CardWeapon cw = notifyWeaponRefill.getCw();

        if(ClientContext.get().getMap() != null)
        {
            try{
                ClientContext.get().getMap().getSquare(notifyWeaponRefill.getX(),notifyWeaponRefill.getY()).addWeapon(Collections.singletonList(cw));
            }catch(MapOutOfLimitException e)
            {
                System.out.println("ERROR: tried to refill outside of game map");
            }
        }

    }

    @Override
    public void handle(NotifyAmmoRefill notifyAmmoRefill){
        CardAmmo ca = notifyAmmoRefill.getCa();

        if(ClientContext.get().getMap() != null)
            try{
                ClientContext.get().getMap().getSquare(notifyAmmoRefill.getX(),notifyAmmoRefill.getY()).setCardAmmo(ca);
            }catch(MapOutOfLimitException e)
            {
                System.out.println("ERROR: tried to refill outside of game map");
            }
    }

    @Override
    public void handle(UpdateMarks updateMarks) {
        Player p = ClientContext.get().getMap().getPlayerById(updateMarks.getId());
        if(p != null)
            p.updateMarks();
    }

    @Override
    public void handle(ChoosePowerUpUsed choosePowerUpUsed) {
        ClientContext.get().getMyPlayer().removePowerUp(Collections.singletonList(choosePowerUpUsed.getCardPower()));
    }

    @Override
    public void handle(AddPayment addPayment) {
        Player me = ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID());
        try {
            me.pay(addPayment.getAmmo(),addPayment.getPowers());
        }
        catch(InsufficientAmmoException e){
            e.printStackTrace();
        }
    }

    @Override
    public void handle(NotifyPlayerExitedWaitingRoom notifyPlayerExitedWaitingRoom) {
        ClientContext.get().getPlayersInWaiting().stream().filter(pl -> pl.getId() == notifyPlayerExitedWaitingRoom.getPlayerId()).findFirst().ifPresent(clientView::notifyPlayerLeavedWaitingRoom);

    }

    @Override
    public void handle(NotifyPlayerJoinedWaitingRoom notifyPlayerJoinedWaitingRoom) {
        Player p = notifyPlayerJoinedWaitingRoom.getPlayer();
        ClientContext.get().getPlayersInWaiting().add(p);
        clientView.notifyPlayerJoinedWaitingRoom(p);
    }

    public void resumeState(){
        switch(state){
            case WAITING_GRAB_WEAPON:
                client.sendMessage(new GrabActionRequest());
        }
    }

    public boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Stop listening before closing the application
     */
    public void stopListening() {
        client.close();
    }

    /**
     * Called by the newtork layer in case of connection error
     */
    public void manageConnectionError() {
        client.stopWaitingPing();
        clientView.notifyConnectionError();
    }

    /**
     * Reconnect with the server after a connection error
     */
    public void retryConnection() {
        if(!client.init())
            clientView.notifyConnectionError();
        else
            startNewGame();
    }

    /**
     * Start a new game
     */
    public void startNewGame() {
        state  = ClientState.WAITING_START;
        client.sendMessage(new GetAvailableMapsRequest());
    }
}
