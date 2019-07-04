package game.controller;

import game.controller.commands.ClientGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.clientcommands.GetAvailableMapsRequest;
import game.controller.commands.clientcommands.GrabActionRequest;
import game.controller.commands.clientcommands.LoginMessage;
import game.controller.commands.clientcommands.RejoinGameResponse;
import game.controller.commands.servercommands.*;
import game.model.*;
import game.model.exceptions.InsufficientAmmoException;
import game.model.exceptions.MapOutOfLimitException;
import game.model.exceptions.NoCardWeaponSpaceException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * It's the controller on the clientNetwork side,
 * the link among the network layer, the gui and the ClientContext
 */
public class ClientController implements ServerGameMessageHandler {
    /** Reference to the network layer */
    private final ClientNetwork clientNetwork;
    /** Reference to the view */
    private View clientView;
    /** List of action currently available for the player */
    private List<Action> availableActions;
    /** Current state of the clientNetwork */
    private ClientState state;
    /** True when the game is started*/
    private boolean gameStarted;
    /** True when the game is terminated*/
    private boolean gameEnded;
    /** True during the reconnection phase*/
    private boolean reconnecting;

    /**
     * Create the ClientController
     * @param clientNetwork network layer
     * @param view view layer
     */
    public ClientController(ClientNetwork clientNetwork, View view) {
        this.clientNetwork = clientNetwork;
        this.gameStarted = false;
        this.clientView = view;
        this.clientView.setController(this);
        this.state = ClientState.WAITING_START;
        this.gameEnded = false;
        this.reconnecting = false;
    }

    /**
     * Return the corresponding network layer
     * @return the ClientNetwork object
     */
    public ClientNetwork getClientNetwork() {
        return clientNetwork;
    }

    /**
     * Return the ClientController state
     * @return the actual ClientState
     */
    ClientState getState() {
        return state;
    }

    /**
     * Set the ClientController state
     * @param state The new ClientState
     */
    void setState(ClientState state) {
        this.state = state;
    }

    /**
     * Return a list of the available Actions in the current phase
     * @return the list of the available actions
     */
    List<Action> getAvailableActions() {
        return availableActions;
    }


    /**
     * Start the connection listening and the identification phase
     */
    public void run() {
        clientNetwork.startListening(this);
        clientView.setUserNamePhase();
    }

    /**
     * Send a series of messages to server
     * @param messages list of messages to be sent
     */
    void sendMessages(List<ClientGameMessage> messages)
    {
        for(ClientGameMessage m : messages)
            clientNetwork.sendMessage(m);
    }

    /**
     * Update the ClientContext reloading the weapon and removing the power-up cards and ammo cards used by the player
     * @param serverMsg message from the server
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
     * @param serverMsg message from the server
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
     * @param serverMsg message from the server
     */
    @Override
    public void handle(ChooseSquareRequest serverMsg) {
        clientView.chooseSquarePhase(serverMsg.getPossiblePositions());
    }

    /**
     * Ask the player what target want to select to apply his action
     * @param serverMsg message from the server
     */
    @Override
    public void handle(ChooseTargetRequest serverMsg) {
        ClientContext.get().setCurrentEffect(serverMsg.getCurrSimpleEffect());
        clientView.chooseTargetPhase(serverMsg.getPossibleTargets());
    }

    /**
     * Ask the player what action want to choose for this turn phase
     * @param serverMsg message from the server
     */
    @Override
    public void handle(ChooseTurnActionRequest serverMsg) {

        this.state = ClientState.WAITING_ACTION;
        ClientContext.get().setMovedAllowed(serverMsg.isMovAllowed());
        clientView.chooseTurnActionPhase(serverMsg.isMovAllowed());
    }

    /**
     * Notify the player that the targets chosen are invalid
     * @param serverMsg message from the server
     */
    @Override
    public void handle(InvalidTargetResponse serverMsg) {
        clientView.invalidTargetNotification();
    }

    /**
     * Notify the player the invalid weapon selection
     * @param serverMsg message from the server
     */
    @Override
    public void handle(InvalidWeaponResponse serverMsg) {
        clientView.invalidWeaponNotification();

    }

    /**
     * Notify the player the invalid action selection
     * @param serverMsg message from the server
     */
    @Override
    public void handle(InvalidActionResponse serverMsg) {
        clientView.invalidActionNotification();
    }

    /**
     * Notify the player that he doesn't have other new actions available
     * @param serverMsg message from the server
     */
    @Override
    public void handle(InsufficientNumberOfActionResponse serverMsg) {
        clientView.insufficientNumberOfActionNotification();
    }

    /**
     * Notify the player the invalid step selection
     * @param serverMsg message from the server
     */
    @Override
    public void handle(InvalidStepResponse serverMsg) {
        clientView.invalidStepNotification();
    }

    /**
     * Notify the player that he doesn't have enough space to grab new weapons
     * @param serverMsg message from the server
     */
    @Override
    public void handle(MaxNumberOfWeaponsResponse serverMsg) {
        clientView.maxNumberOfWeaponNotification();
    }

    /**
     * Notify the player of a damage occurred in the game
     * @param serverMsg message from the server
     */
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
            if (!counterattack.isEmpty() && this.state != ClientState.WAITING_COUNTERATTACK && hitten.getDamage().size()<=10) //<=10
            {
                this.state = ClientState.WAITING_COUNTERATTACK;
                clientView.chooseCounterAttack(counterattack, shooter);
            }
        }
    }

    /**
     * Notify the clientNetwork about a death happened in the game
     * @param serverMsg message from the server
     */
    @Override
    public void handle(NotifyDeathResponse serverMsg) {
        Player killer = ClientContext.get().getMap().getPlayerById(serverMsg.getIdKiller());
        Player victim = ClientContext.get().getMap().getPlayerById(serverMsg.getIdVictim());
        Kill k = new Kill(killer,victim,serverMsg.isRage());
        ClientContext.get().getKillboard().add(k);
        clientView.notifyDeath(serverMsg.getIdKiller(),serverMsg.getIdVictim(),serverMsg.isRage());
    }

    /**
     * Show the final results of the game
     * @param serverMsg message from the server
     */
    @Override
    public void handle(NotifyEndGame serverMsg) {
        if(this.state != ClientState.GAME_END && this.state != ClientState.WAITING_START)
        {
            this.gameEnded = true;
            this.state = ClientState.GAME_END;
            clientView.showRanking(serverMsg.getRanking());
        }
    }


    /**
     * Notify the clientNetwork about the grab of a weapon during the game
     * @param serverMsg message from the server
     */
    @Override
    public void handle(NotifyGrabWeapon serverMsg) {
        ClientContext instance = ClientContext.get();
        try {
            instance.getMap().getSquare(serverMsg.getX(),serverMsg.getY()).getWeapons().remove(serverMsg.getCw());
            if(serverMsg.getwWaste() != null)
                instance.getMap().getSquare(serverMsg.getX(),serverMsg.getY()).getWeapons().add(serverMsg.getwWaste());
        } catch (MapOutOfLimitException e) {
            System.out.println("Someone grab an ammo outside of the map :/");
        }
        clientView.grabWeaponNotification(serverMsg.getP(),serverMsg.getCw().getName(),serverMsg.getX(),serverMsg.getY());

    }

    /**
     * Notify the clientNetwork about a movement happened during the game
     * @param serverMsg message from the server
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
            System.out.println("Someone moved out of the map :/");
        }
    }

    /**
     * Notify the clientNetwork about the use of a power up during the game
     * @param serverMsg message from the server
     */
    @Override
    public void handle(NotifyPowerUpUsage serverMsg) {
        ClientContext instance = ClientContext.get();
        if(instance.getMyID() == serverMsg.getP().getId()){
            instance.getMap().getPlayerById(serverMsg.getP().getId()).getCardPower().remove(serverMsg.getCp());
        }
        clientView.powerUpUsageNotification(serverMsg.getP().getNickName(),serverMsg.getCp().getEffect().getName(),serverMsg.getCp().getEffect().getDescription());
    }

    /**
     * Receive the confirm of the grab of an ammo card, updating the ClientContext
     * @param serverMsg message from the server
     */
    @Override
    public void handle(PickUpAmmoResponse serverMsg) {
        //notify already sets null the card-ammo on the square
        for(Color c : serverMsg.getColors())
            ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).addAmmo(c);
        if(!serverMsg.getPowerups().isEmpty())
            for(CardPower cp : serverMsg.getPowerups())
                ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).addCardPower(cp);
        clientView.notifyCompletedOperation("");
    }

    /**
     * Receive the confirm of the grab of a weapon, updating the ClientContext
     * @param serverMsg message from the server
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
     * Manage the respawn of the player
     * @param serverMsg message from the server
     */
    @Override
    public void handle(RespawnRequest serverMsg) {
        Player p = ClientContext.get().getPlayersInWaiting().stream().filter(pl -> pl.getId() == ClientContext.get().getMyID()).findFirst().orElse(ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()));

        if(serverMsg.getcPU() != null)
            p.addCardPower(serverMsg.getcPU());

        clientView.choosePowerUpToRespawn(p.getCardPower());

    }

    /**
     * Alert about the insufficient ammo of the player to complete the previously selected operation
     * @param serverMsg message from the server
     */
    @Override
    public void handle(InsufficientAmmoResponse serverMsg) {
        clientView.insufficientAmmoNotification();
    }

    /**
     * Alert about the successful completion of a previously selected operation
     * @param serverMsg message from the server
     */
    @Override
    public void handle(OperationCompletedResponse serverMsg) {
        if(!serverMsg.getMessage().equals(""))
            clientView.notifyCompletedOperation(serverMsg.getMessage());
    }

    /**
     * Alert about an invalid selection of power up in the previously selected action
     * @param serverMsg message from the server
     */
    @Override
    public void handle(InvalidPowerUpResponse serverMsg) {
        clientView.notifyInvalidPowerUP();
        if(this.state == ClientState.WAITING_ACTION)
            clientView.chooseTurnActionPhase(ClientContext.get().isMovedAllowed());
    }

    /**
     * Alert about an invalid position indicated in a previously selected grab action
     * @param serverMsg message from the server
     */
    @Override
    public void handle(InvalidGrabPositionResponse serverMsg) {
        clientView.notifyInvalidGrabPosition();
    }

    /**
     * Ask the player to use a power up after doing a damage (Targeting scope)
     * @param serverMsg message from the server
     */
    @Override
    public void handle(AfterDamagePowerUpRequest serverMsg){
        clientView.choosePowerUpToUse(ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID()).getCardPower());
    }

    /**
     * Notify about the start of the game, saving game datas in ClientContext
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
        /*if(ClientContext.get().getMyPlayer().getCardPower() == null)
        {
            System.out.println("NON HO LE MIE CARD POWER =( !");
            for(Player p : serverMsg.getPlayers())
            {
                System.out.println("Player " + p.getId() + " powercard= " + p.getCardPower());
            }
        }*/
        clientView.notifyStart();
    }

    /**
     * Start the waiting room selection phase
     * @param waitingRoomsListResponse message from the server
     */
    @Override
    public void handle(WaitingRoomsListResponse waitingRoomsListResponse) {
        clientView.chooseRoomPhase(waitingRoomsListResponse.getAvaiableWaitingRooms());
    }

    /**
     * Alert about the previous sending of an invalid message
     * @param invalidMessageResponse message from the server
     */
    @Override
    public void handle(InvalidMessageResponse invalidMessageResponse) {
        clientView.notifyInvalidMessage();
    }

    /**
     *  Notify about the change of turn during the game
     * @param notifyTurnChanged message from the server
     */
    @Override
    public void handle(NotifyTurnChanged notifyTurnChanged) {
        if(notifyTurnChanged.getCurrPlayerId() != ClientContext.get().getMyID())
            this.state = ClientState.WAITING_TURN;
        else
            this.state = ClientState.WAITING_ACTION;
        clientView.notifyTurnChanged(notifyTurnChanged.getCurrPlayerId());
    }

    /**
     * Notify about marks given during the game
     * @param notifyMarks message from the server
     */
    @Override
    public void handle(NotifyMarks notifyMarks) {
        Player shooter = ClientContext.get().getMap().getPlayerById(notifyMarks.getShooterId());
        ClientContext.get().getMap().getPlayerById(notifyMarks.getHitId()).addThisTurnMarks(shooter,notifyMarks.getMarks());
        clientView.notifyMarks(notifyMarks.getMarks(),notifyMarks.getHitId(),notifyMarks.getShooterId());
    }

    /**
     * Notify about the grab of an ammo card during the game
     * @param notifyGrabCardAmmo message from the server
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
     * Notify about the spawn of a player
     * @param notifyRespawn message from the server
     */
    @Override
    public void handle(NotifyRespawn notifyRespawn) {
        Player p = ClientContext.get().getPlayersInWaiting().stream().filter(pl -> pl.getId() == notifyRespawn.getpId()).findFirst().orElse(null);
        if(p == null){
            p = ClientContext.get().getMap().getPlayerById(notifyRespawn.getpId());
        }
        if(ClientContext.get().isFinalFrenzy()){
            p.setBeforeFrenzy(false);
        }
        try {
            ClientContext.get().getMap().movePlayer(p,notifyRespawn.getX(),notifyRespawn.getY());
        } catch (MapOutOfLimitException e) {
            e.printStackTrace();
        }
        if(p.getId() == ClientContext.get().getMyID())
            this.state = ClientState.WAITING_ACTION;
        ClientContext.get().getPlayersInWaiting().remove(p);
        p.getDamage().clear();
        clientView.notifyRespawn(notifyRespawn.getpId());
    }

    /**
     * Show the list of available game maps sent by the server
     * @param availableMapsListResponse message from the server
     */
    @Override
    public void handle(AvailableMapsListResponse availableMapsListResponse) {
        clientView.showMapsPhase(availableMapsListResponse.getAvaiableMaps());
    }

    /**
     * Receive the confirm of entering in a waiting room
     * @param joinWaitingRoomResponse message from the server
     */
    @Override
    public void handle(JoinWaitingRoomResponse joinWaitingRoomResponse) {
        ClientContext.get().setMyID(joinWaitingRoomResponse.getId());
        ClientContext.get().setPlayersInWaiting(new ArrayList(joinWaitingRoomResponse.getWaitingRoom().getPlayers()));
        clientView.notifyCompletedOperation("You correctly joined the waiting room! Wait for other players...");
    }

    /**
     * Receive the confirm of the creation of a waiting room
     * @param createWaitingRoomResponse message from the server
     */
    @Override
    public void handle(CreateWaitingRoomResponse createWaitingRoomResponse) {
        ClientContext.get().setMyID(createWaitingRoomResponse.getId());
        ClientContext.get().setPlayersInWaiting(new ArrayList<>());
        clientView.notifyCompletedOperation("Waiting room correctly created! \n>>Wait for other players...");
        clientView.waitStart();
    }

    /**
     * Receive the list of the weapons available to be grabbed
     * @param chooseWeaponToGrabRequest message from the server
     */
    @Override
    public void handle(ChooseWeaponToGrabRequest chooseWeaponToGrabRequest){
        state = ClientState.WAITING_GRAB_WEAPON;
        clientView.chooseWeaponToGrab(chooseWeaponToGrabRequest.getWeapons());
    }

    /**
     * Start the weapon realoding phase
     * @param reloadWeaponAsk message from the server
     */
    @Override
    public void handle(ReloadWeaponAsk reloadWeaponAsk) {
        this.state = ClientState.WAITING_FINAL_RELOAD;
        if(!reloadWeaponAsk.getWeaponsToReload().isEmpty())
            clientView.reloadWeaponPhase(reloadWeaponAsk.getWeaponsToReload());
        else
            clientView.showNoWeaponToReload();
    }

    /**
     * Notify the suspension of a player
     * @param notifyPlayerSuspend message from the server
     */
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

    /**
     * Alert about the presence of another player with the previously sent username, restarting the login phase
     * @param userAlreadyLoggedResponse message from the server
     */
    @Override
    public void handle(UserAlreadyLoggedResponse userAlreadyLoggedResponse) {
        clientView.alreadyLoggedPhase();
    }

    /**
     * Receive the login confirm
     * @param userLoggedResponse message from the server
     */
    @Override
    public void handle(UserLoggedResponse userLoggedResponse) {
        clientView.loginCompletedPhase();
    }

    /**
     * Receive the request to rejoin a game from which the user has been suspended
     * @param rejoinGameRequest message from the server
     */
    @Override
    public void handle(RejoinGameRequest rejoinGameRequest) {
        if(this.reconnecting)
        {
            clientNetwork.sendMessage(new RejoinGameResponse(true,ClientContext.get().getUser()));
            clientView.notifyReconnected();
            this.reconnecting = false;
        }
        else
            clientView.rejoinGamePhase(rejoinGameRequest.getOtherPlayerNames());
    }

    /**
     * Notify about the rejoin of a previously suspended player
     * @param notifyPlayerRejoin message from the server
     */
    @Override
    public void handle(NotifyPlayerRejoin notifyPlayerRejoin) {
        Player p = ClientContext.get().getPlayersInWaiting().stream().filter(pl -> pl.getId() == notifyPlayerRejoin.getPlayerId()).findFirst().orElse(ClientContext.get().getMap().getPlayerById(notifyPlayerRejoin.getPlayerId()));
        if(p!=null)
        {
            p.setSuspended(false);
            clientView.notifyPlayerRejoin(p);
        }

    }

    /**
     * Receive the confirm to be rejoined
     * @param rejoinGameConfirm message from the server
     */
    @Override
    public void handle(RejoinGameConfirm rejoinGameConfirm) {
        this.state = ClientState.WAITING_TURN;
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

    /**
     * Receive the list of available weapons to be used in a shoot action
     * @param chooseWeaponToShootRequest message from the server
     */
    @Override
    public void handle(ChooseWeaponToShootRequest chooseWeaponToShootRequest) {
        state = ClientState.WAITING_SHOOT;
        clientView.chooseWeaponToShoot(chooseWeaponToShootRequest.getMyWeapons());
    }

    /**
     * Start of the selection of the first effect during the shoot action
     * @param chooseFirstEffectRequest message from the server
     */
    @Override
    public void handle(ChooseFirstEffectRequest chooseFirstEffectRequest) {
        clientView.chooseFirstEffect(chooseFirstEffectRequest.getBaseEff(),chooseFirstEffectRequest.getAltEff());
    }

    /**
     * Ask if the user want to use a plus effect which can be used before the base effect
     * @param beforeBaseRequest message from the server
     */
    @Override
    public void handle(BeforeBaseRequest beforeBaseRequest) {
        clientView.usePlusBeforeBase(beforeBaseRequest.getPlusEff());
    }

    /**
     * Ask about the use of plus effects
     * @param usePlusEffectRequest message from the server
     */
    @Override
    public void handle(UsePlusEffectRequest usePlusEffectRequest) {
        clientView.choosePlusEffect(usePlusEffectRequest.getPlusEffects());
    }

    /**
     * Ask about the use of plus effects, respecting their given order
     * @param usePlusByOrderRequest message from the server
     */
    @Override
    public void handle(UsePlusByOrderRequest usePlusByOrderRequest) {
        clientView.usePlusInOrder(usePlusByOrderRequest.getPlusEffects());
    }

    /**
     * Receive the confirm of a previously requested shoot action
     * @param shootActionResponse message from the server
     */
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

    /**
     * Receive the request to remove the power-up used to spawn
     * @param removeSpawnPowerUp message from the server
     */
    @Override
    public void handle(RemoveSpawnPowerUp removeSpawnPowerUp) {
        Player me = ClientContext.get().getMap().getPlayerById(ClientContext.get().getMyID());
        //if(me.getCardPower().contains(removeSpawnPowerUp.getPowerup()))
            me.getCardPower().remove(removeSpawnPowerUp.getPowerup());
    }

    /**
     * Refill the gam map with a weapon
     * @param notifyWeaponRefill message from the server
     */
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

    /**
     * Refill the game map with a card ammo
     * @param notifyAmmoRefill message from the server
     */
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

    /**
     * Update player marks
     * @param updateMarks message from the server
     */
    @Override
    public void handle(UpdateMarks updateMarks) {
        Player p = ClientContext.get().getMap().getPlayerById(updateMarks.getId());
        if(p != null)
            p.updateMarks();
    }

    /**
     * Remove the indicated power-up, previously used
     * @param choosePowerUpUsed message from the server
     */
    @Override
    public void handle(ChoosePowerUpUsed choosePowerUpUsed) {
        ClientContext.get().getMyPlayer().removePowerUp(Collections.singletonList(choosePowerUpUsed.getCardPower()));
    }

    /**
     * Remove munitions and/or power-up cards used for a payment
     * @param addPayment
     */
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

    /**
     * Update player points
     * @param notifyPoints message from the server
     */
    @Override
    public void handle(NotifyPoints notifyPoints) {
        ClientContext.get().getMyPlayer().setPoints(notifyPoints.getPoints());
        clientView.showPoints();
    }

    /**
     * Notify about a rage hit
     * @param notifyRage message from the server
     */
    @Override
    public void handle(NotifyRage notifyRage) {
        clientView.notifyRage(notifyRage.getKiller(), notifyRage.getVictim());
    }

    @Override
    public void handle(NotifyFinalFrenzy notifyFinalFrenzy) {
        for(Player p : ClientContext.get().getMap().getAllPlayers()){
            if(!p.getDamage().isEmpty() || !p.getMark().isEmpty())
                p.setBeforeFrenzy(true);
            else
                p.setBeforeFrenzy(false);
        }
        ClientContext.get().setFinalFrenzy();
        clientView.notifyFinalFrenzy();
    }

    /**
     * Notify about the leaving of the waiting room by a player
     * @param notifyPlayerExitedWaitingRoom message from the server
     */
    @Override
    public void handle(NotifyPlayerExitedWaitingRoom notifyPlayerExitedWaitingRoom) {
        ClientContext.get().getPlayersInWaiting().stream().filter(pl -> pl.getId() == notifyPlayerExitedWaitingRoom.getPlayerId()).findFirst().ifPresent(clientView::notifyPlayerLeavedWaitingRoom);

    }

    /**
     * Notify about the join of a player in the waiting room
     * @param notifyPlayerJoinedWaitingRoom message from the server
     */
    @Override
    public void handle(NotifyPlayerJoinedWaitingRoom notifyPlayerJoinedWaitingRoom) {
        Player p = notifyPlayerJoinedWaitingRoom.getPlayer();
        ClientContext.get().getPlayersInWaiting().add(p);
        clientView.notifyPlayerJoinedWaitingRoom(p);
    }

    /**
     * Return to grab weapon state, resending the grab request
     */
    void resumeGrabState(){
        switch(state){
            case WAITING_GRAB_WEAPON:
                clientNetwork.sendMessage(new GrabActionRequest());
        }
    }

    /**
     * Return if the game is started
     * @return true if it is started, false otherwise
     */
    boolean isGameStarted() {
        return gameStarted;
    }

    /**
     * Stop listening before closing the application
     */
    void stopListening() {
        clientNetwork.close();
    }

    /**
     * Called by the network layer in case of connection error
     */
    void manageConnectionError() {
        clientNetwork.stopWaitingPing();
        clientView.notifyConnectionError();
    }

    /**
     * Reconnect with the server after a connection error
     */
    void retryConnection() {
        if(!clientNetwork.init())
            clientView.notifyConnectionError();
        else{
            clientNetwork.startListening(this);
            this.reconnecting = true;
            clientNetwork.sendMessage(new LoginMessage(ClientContext.get().getMyPlayer().getNickName(),true));
        }
    }

    /**
     * Start a new game
     */
    void startNewGame() {
        state  = ClientState.WAITING_START;
        clientNetwork.sendMessage(new GetAvailableMapsRequest());
    }
}
