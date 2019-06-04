package game.controller;

import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.controller.commands.ServerMessageHandler;
import game.controller.commands.clientcommands.*;
import game.controller.commands.servercommands.*;
import game.model.*;
import game.model.effects.*;

import java.util.ArrayList;
import java.util.Collections;


import game.model.exceptions.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ServerController implements ClientMessageHandler, PlayerObserver {
    // reference to the Networking layer
    private SocketClientHandler clientHandler;

    // reference to the Model
    private Game model;
    private Player currPlayer;
    private List<CardWeapon> playerWeapons;
    private CardWeapon selectedWeapon;
    private List<FullEffect> remainingPlusEffects;
    private FullEffect plusBeforeBase;
    private boolean isOrdered;
    private List<Color> ammoToPay;
    private List<CardPower> powerUpToPay;
    private FullEffect currFullEffect;
    private SimpleEffect currSimpleEffect;
    private Player toBeMoved;
    private boolean baseDone;
    private int nSimpleEffect; //the number of the current simpleEffect of the current FullEffect
    private List<Target> selectableTarget;
    private List<Square> selectableSquares;
    private ServerState state;
    private List<Action> avaialableActionSteps;
    private int numberOfTurnActionMade;
    private WaitingRoom waitingRoom;
    private String nickname;


    public ServerController(SocketClientHandler clientHandler) {

        this.clientHandler = clientHandler;
        this.state = ServerState.JUST_LOGGED;
    }

    public ServerController(){ }

    public SocketClientHandler getClientHandler() {
        return clientHandler;
    }

    public ServerState getState() {
        return state;
    }

    public WaitingRoom getWaitingRoom() {
        return waitingRoom;
    }

    public void setWaitingRoom(WaitingRoom waitingRoom) {
        this.waitingRoom = waitingRoom;
        this.state = ServerState.WAITING_FOR_PLAYERS;
    }

    /**
     * Called by the WaitingRoom when it's ready to start the game, it notify the client associated to this server controller that the game is started, sending the Game serialized object
     *
     * @param g
     */
    void startGame(Game g, Player p)
    {
        state = ServerState.WAITING_SPAWN;
        this.model = g;
        this.currPlayer = p;
        this.numberOfTurnActionMade = 0;
        CardPower c1 = g.drawPowerUp();
        this.currPlayer.addCardPower(c1);
        this.currPlayer.addAmmo(Color.BLUE);
        this.currPlayer.addAmmo(Color.YELLOW);
        this.currPlayer.addAmmo(Color.RED);
    }

    public Game getModel() {
        return model;
    }

    public ServerMessage handle(CheckValidWeaponRequst clientMsg){
        List<CardWeapon> cardWeapon = new ArrayList<>();
        for(CardWeapon cw : currPlayer.getWeapons()){
            if(cw.checkweapon(clientMsg.getPlayer())){
                cardWeapon.add(cw);
                System.out.println("added" + cw.toString());
            }
        }
        return new OperationCompletedResponse();
    }

    @Override
    public ServerMessage handle(ChooseSquareToShootResponse chooseSquareToShootResponse) {
        List<Square> selectedTargets = chooseSquareToShootResponse.getChoosenSquare();
        List<Target> toApplyEffect = null;
        if(!selectableTarget.containsAll(selectedTargets)) {
            clientHandler.sendMessage(new InvalidTargetResponse());
            return checkTurnEnd();
        }/*
        if(!validateSelectedTargets(selectedTargets,currSimpleEffect)){
            clientHandler.sendMessage(new InvalidTargetResponse());
            return checkTurnEnd();
        }*/
        //in case no target has been selected (and it's allowed) it stops
        if(selectedTargets.isEmpty())
            return new OperationCompletedResponse();
        toApplyEffect = new ArrayList<>();
        for(Square t : model.getMap().getAllSquares())
            if(selectedTargets.contains(t))
                toApplyEffect.addAll(t.getPlayers());
        return handleTargetSelection(toApplyEffect,currSimpleEffect);
    }


    public Player getCurrPlayer() {
        return currPlayer;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public ServerMessage handle(ChooseSquareResponse clientMsg) throws MapOutOfLimitException {
        Square selectedSquare = model.getMap().getSquare(clientMsg.getSelectedSquare().getX(),clientMsg.getSelectedSquare().getY());
        boolean correctSquare = false;
        for(Square s : selectableSquares)
            if(s.equals(selectedSquare))
                correctSquare = true;
        if(!correctSquare) {
            clientHandler.sendMessage(new InvalidTargetResponse());
            return checkTurnEnd();
        }

        currSimpleEffect.applyEffect(toBeMoved,Collections.singletonList(selectedSquare));
        if(currFullEffect != null)
            return handleEffect(currFullEffect.getSimpleEffects().get(nSimpleEffect));

        //return new NotifyMovement(currPlayer.getId(),clientMsg.getSelectedSquare().getX(),clientMsg.getSelectedSquare().getY());
        if(state  == ServerState.HANDLING_MOVEMENT) {
            if (!avaialableActionSteps.isEmpty())
                return new ChooseSingleActionRequest(avaialableActionSteps);
            else {
                return checkTurnEnd();
            }
        }
        else{
            //TODO: we should ask to continue with the effects in case of a movement effect and not a movement action
            return  new OperationCompletedResponse("Effect completed, this message will be replaced..");
        }


    }

    void endTurnManagement()
    {
        //state
        List<Player> toBeRespawned = model.changeTurn();
        toBeRespawned.forEach(p -> p.notifyRespawn());
        if(model.getnPlayerToBeRespawned() == 0)
            model.getCurrentTurn().newTurn(false); //TODO: check final frezy
    }

    /**
     * Validate the selected target for an effect and apply it, or ask for the movement position if it's a MovementEffect
     * @param clientMsg
     * @return
     */
    @Override
    public ServerMessage handle(ChooseTargetResponse clientMsg)
    {
        List<Target> selectedTargets = clientMsg.getSelectedTargets();
        List<Target> toApplyEffect = null;
        if(!selectableTarget.containsAll(selectedTargets)) {
            clientHandler.sendMessage(new InvalidTargetResponse());
            return checkTurnEnd();
        }
        if(!validateSelectedTargets(selectedTargets,currSimpleEffect)){
            clientHandler.sendMessage(new InvalidTargetResponse());
            return checkTurnEnd();
        }
        //in case no target has been selected (and it's allowed) it stops
        if(selectedTargets.isEmpty())
            return new OperationCompletedResponse();
        toApplyEffect = new ArrayList<>();
        for(Target t : model.getPlayers())
            if(selectedTargets.contains(t))
                toApplyEffect.add(t);
        return handleTargetSelection(toApplyEffect,currSimpleEffect);
    }

    /**
     * Validate the client target selection for a generic simpleEffect, checking the maxEnemy - minEnemy constraint
     * @param selectedTarget
     * @param e
     * @return
     */
    private boolean validateSelectedTargets(List<Target> selectedTarget, SimpleEffect e)
    {
        if(selectedTarget.size() > e.getMaxEnemy() || (selectedTarget.size() < e.getMinEnemy() && selectableTarget.size() >= e.getMinEnemy()))
            return false;
        return true;
    }

    /**
     * Validate the client target selection for an AreaDamageEffect
     * @param selectedTarget
     * @param e
     * @return true if the selection is correct, false otherwise
     */
    private boolean validateSelectedTargets(List<Target> selectedTarget, AreaDamageEffect e)
    {
        if(selectedTarget.size() > e.getMaxEnemy() || (selectedTarget.size() < e.getMinEnemy() && selectableTarget.size() >= e.getMinEnemy()))
            return false;

        //Check maxEnemyPerSquare constraint
        List<Player> selectedPlayer = selectedTarget.stream().map( t-> (Player)t).collect(Collectors.toList());
        Map<Square, List<Player>> targetPerSquare = selectedPlayer.stream().collect(Collectors.groupingBy(Player::getPosition));
        for(List<Player> l : targetPerSquare.values())
            if(l.size() > e.getMaxEnemyPerSquare())
                return false;

        return true;
    }

    /**
     * Apply a simpleEffect and manage the next simple effect, if there is any
     * @param selectedTarget
     * @param s
     * @return
     */
    private ServerMessage handleTargetSelection(List<Target> selectedTarget, SimpleEffect s)
    {
        s.applyEffect(currPlayer,selectedTarget);
        return terminateFullEffect();
    }

    private ServerMessage checkShootActionEnd(){
        nSimpleEffect = 0;
        if(!baseDone)
            return firstEffect();
        else if(remainingPlusEffects != null)
            if(!remainingPlusEffects.isEmpty())
                return managePlusEffectChoice();
        return checkTurnEnd();
    }

    /**
     * Ask for the position selection for a MovementEffect, if necessary, otherwise it applies the effect and proceed to the next simple effect, if there is any
     * @param selectedTarget
     * @param s
     * @return
     */
    private ServerMessage handleTargetSelection(List<Target> selectedTarget, MovementEffect s)
    {
        toBeMoved = (Player)selectedTarget.get(0);
        List<Square> selectableSquares = s.selectPosition(currPlayer);

        if(selectableSquares.isEmpty())
            return new InvalidWeaponResponse();
        if(selectableSquares.size() > 1)
            return new ChooseSquareRequest(selectableSquares);

        //apply without asking nothing if it is not necessary
        s.applyEffect((Player)selectableTarget.get(0),Collections.singletonList(selectableSquares.get(0)));
        if(currFullEffect != null)
            return handleEffect(currFullEffect.getSimpleEffects().get(nSimpleEffect));

        return new OperationCompletedResponse();

    }

    @Override
    public ServerMessage handle(ChooseTurnActionResponse clientMsg) {
        try {
            Action action = clientMsg.getTypeOfAction();
            if (Action.checkAction(action)) {
                switch(action){
                    case MOVEMENT:
                        currFullEffect = null;
                        toBeMoved = currPlayer;
                        break;
                    case GRAB:
                        toBeMoved = currPlayer;
                        break;
                    case SHOOT:
                        break;
                }
                avaialableActionSteps = currPlayer.getGame().getCurrentTurn().newAction(clientMsg.getTypeOfAction(), currPlayer.getAdrenaline(),model.isFinalFreazy());
                return new ChooseSingleActionRequest(avaialableActionSteps);
            } else {
                return new InvalidActionResponse();
            }
        }
        catch(NoResidualActionAvaiableException e){
            return new InsufficientNumberOfActionResponse();
        }

    }
    @Override
    public ServerMessage handle(GrabActionRequest clientMsg) throws NoCardAmmoAvailableException {
        List<CardWeapon> possibleToGrab = new ArrayList<>();
        if (currPlayer.getPosition().isRespawn()) {
            if (currPlayer.getPosition().getWeapons() != null) {
                for (CardWeapon cw : currPlayer.getPosition().getWeapons())
                    if (currPlayer.canGrabWeapon(cw))
                        possibleToGrab.add(cw);
                if (!possibleToGrab.isEmpty()) {
                    state = ServerState.HANDLING_GRAB;
                    return new ChooseWeaponToGrabRequest(possibleToGrab);
                } else {
                    clientHandler.sendMessage(new InsufficientAmmoResponse());
                    return checkTurnEnd();
                }
            }
        } else {
            if (currPlayer.getPosition().getCardAmmo() != null) {
                CardAmmo toGrab = currPlayer.getPosition().getCardAmmo();
                List<Color> listA = new ArrayList<>(toGrab.getAmmo());
                List<CardPower> listCp = new ArrayList<>(currPlayer.pickUpAmmo());
                state = ServerState.HANDLING_GRAB;
                clientHandler.sendMessage(new PickUpAmmoResponse(listA,listCp));
                if(model.getCurrentTurn().getNumOfActions()>0){
                    state = ServerState.WAITING_ACTION;
                    return new ChooseTurnActionRequest();
                }
                else {
                    return checkTurnEnd();
                }
            }
        }
        clientHandler.sendMessage(new InvalidGrabPositionResponse());
        return checkTurnEnd();
    }

    @Override
    public ServerMessage handle(MovementActionRequest clientMsg) {
        if(model.getCurrentTurn().applyStep(Action.MOVEMENT)){
            currSimpleEffect = new MovementEffect();
            avaialableActionSteps = model.getCurrentTurn().getActionList();
            selectableSquares = currPlayer.getPosition().getSquaresInDirections(1,1);
            this.state = ServerState.HANDLING_MOVEMENT;
            return new ChooseSquareRequest(selectableSquares);
        }
        else{
            clientHandler.sendMessage(new InvalidStepResponse());
            return checkTurnEnd();
        }
    }

    @Override
    public ServerMessage handle(PickUpAmmoRequest clientMsg) {
        if(!currPlayer.getGame().getCurrentTurn().applyStep(Action.GRAB)){
            clientHandler.sendMessage(new InvalidStepResponse());
            return checkTurnEnd();
        }
        try{
            currPlayer.pickUpAmmo();
            avaialableActionSteps = model.getCurrentTurn().getActionList();
            return checkTurnEnd();
        }catch (NoCardAmmoAvailableException e){
            return new InvalidGrabPositionResponse();
        }
    }

    /**
     *
     * @param clientMsg
     * @return
     */
    @Override
    public ServerMessage handle(PickUpWeaponRequest clientMsg) {
        boolean correct = false;
        int count=0;
        List<CardPower> tmp = null;
        if(clientMsg.getPowerup() != null)
            tmp = new ArrayList<>(clientMsg.getPowerup());

        for(CardWeapon cw : currPlayer.getPosition().getWeapons()){
            if(cw.equals(clientMsg.getWeapon()))
                correct = true;
        }
        if(!correct)
            return new InvalidWeaponResponse();
        if(clientMsg.getPowerup() != null) {
            for (CardPower cp : currPlayer.getCardPower())
                for (int i = 0; i < tmp.size(); i++)
                    if (cp.equals(tmp.get(i))) {
                        tmp.remove(i);
                        count++;
                    }
            if (count != clientMsg.getPowerup().size())
                return new InvalidPowerUpResponse();
        }
        //This has to be the last check
        if(!currPlayer.getGame().getCurrentTurn().applyStep(Action.GRAB)){
            clientHandler.sendMessage(new InvalidStepResponse());
            return checkTurnEnd();
        }
        try {
            currPlayer.pickUpWeapon(clientMsg.getWeapon(), clientMsg.getWeaponToWaste(), clientMsg.getPowerup());
            playerWeapons = currPlayer.getWeapons();
            avaialableActionSteps = model.getCurrentTurn().getActionList();
            clientHandler.sendMessage(new PickUpWeaponResponse(clientMsg.getWeapon(), clientMsg.getWeaponToWaste(), clientMsg.getPowerup()));
            return checkTurnEnd();
        }catch (InsufficientAmmoException e){
            clientHandler.sendMessage(new InsufficientAmmoResponse());
            return checkTurnEnd();
        }
        catch (NoCardWeaponSpaceException x){
            return new MaxNumberOfWeaponsResponse();
        }
    }

    private ServerMessage checkTurnEnd()
    {
        List<CardWeapon> weaponsToReload;
        if (model.getCurrentTurn().getNumOfActions() > 0) {
            state = ServerState.WAITING_ACTION;
            return new ChooseTurnActionRequest();
        } else {
            if (!currPlayer.getWeapons().isEmpty()) {
                weaponsToReload = currPlayer.hasToReload();
                if(weaponsToReload !=null)
                    for(CardWeapon cw : weaponsToReload)
                        if(!currPlayer.canReloadWeapon(cw))
                            weaponsToReload.remove(cw);
                    if (weaponsToReload != null) {
                        state = ServerState.WAITING_RELOAD;
                        return new ReloadWeaponAsk(weaponsToReload);
                    }
            }
            endTurnManagement();
            //if(model.getnPlayerToBeRespawned() == 0)
            //    model.getCurrentTurn().newTurn(false); //TODO: check final frezy
            if(currPlayer.equals(model.getCurrentTurn().getCurrentPlayer())){
                state = ServerState.WAITING_ACTION;
                return new ChooseTurnActionRequest();
            }
            else
                return new OperationCompletedResponse("Wait for you next turn!");
        }
    }

    /**
     * Reload a weapon, if possible
     * @param clientMsg
     * @return InvalidWeaponResponse if the Player does not own this weapon or if it is already loaded
     *         InsufficientAmmoResponse if the Player does not have enough ammo/power up cards to pay
     *         InvalidPowerUp if the Playr power up are different from the power up given in the message
     *         OperationCompletedResponse if the weapon has been reloaded correctly
     */
    @Override
    public ServerMessage handle(ReloadWeaponRequest clientMsg) {
        CardWeapon w = currPlayer.getWeapons().stream().filter(wp -> wp.getId() == clientMsg.getWeapon().getId()).findFirst().orElse(null);
        int count = 0;
        List<CardPower> tmp = new ArrayList<>(clientMsg.getPowerups());
        if( w == null)
            return new InvalidWeaponResponse();
        if( w.isLoaded())
            return new InvalidWeaponResponse();
        if(clientMsg.getPowerups() != null) {
            for (CardPower cp : currPlayer.getCardPower())
                for (int i = 0; i < tmp.size(); i++)
                    if (cp.equals(tmp.get(i))) {
                        tmp.remove(i);
                        count++;
                    }
            if (count != clientMsg.getPowerups().size())
                return new InvalidPowerUpResponse();
        }
        try{
            w.reloadWeapon(clientMsg.getPowerups());
            state = ServerState.WAITING_TURN;
            return new CheckReloadResponse(clientMsg.getWeapon(), clientMsg.getPowerups());
        }catch(InsufficientAmmoException e)
        {
            return new InsufficientAmmoResponse();
        }
    }

    /*
    @Override
    public ServerMessage handle(SpawnResponse clientMsg){
         if(state == ServerState.WAITING_SPAWN){
             if (currPlayer.getCardPower().contains(clientMsg.getPowerUp())) {
                 currPlayer.respawn(clientMsg.getPowerUp());
                 currPlayer.removePowerUp(Collections.singletonList(clientMsg.getPowerUp()));
                 model.addPowerWaste(clientMsg.getPowerUp());
                 if (model.getCurrentTurn().getCurrentPlayer().equals(currPlayer)) {
                     state = ServerState.WAITING_ACTION;
                     return new ChooseTurnActionRequest();
                 } else {
                     state = ServerState.WAITING_TURN;
                     return new OperationCompletedResponse("Wait for your turn");
                 }
             } else
                 return new InvalidPowerUpResponse();
         }
         else{
             return new InvalidMessageResponse("You are not dead, you can't respawn!");
         }
         }
    */
    @Override
    public ServerMessage handle(RespawnResponse clientMsg) {
        if (currPlayer.isDead() || state == ServerState.WAITING_SPAWN) {
            if (currPlayer.getCardPower().contains(clientMsg.getPowerUp())) {
                currPlayer.respawn(clientMsg.getPowerUp());
                currPlayer.removePowerUp(Collections.singletonList(clientMsg.getPowerUp()));
                model.addPowerWaste(clientMsg.getPowerUp());
                if (state == ServerState.WAITING_SPAWN && model.getCurrentTurn().getCurrentPlayer().equals(currPlayer)) {
                    state = ServerState.WAITING_ACTION;
                    model.getCurrentTurn().startTimer();
                    clientHandler.sendMessage(new RemoveSpawnPowerUp(clientMsg.getPowerUp()));
                    return new ChooseTurnActionRequest();
                }
                else if(state == ServerState.WAITING_RESPAWN)
                {
                    state = ServerState.WAITING_TURN;
                    model.decreaseToBeRespawned();
                    clientHandler.sendMessage(new OperationCompletedResponse("You are respawned!"));
                    clientHandler.sendMessage(new RemoveSpawnPowerUp(clientMsg.getPowerUp()));
                    if(model.getnPlayerToBeRespawned() == 0)
                        model.getCurrentTurn().newTurn(false); //TODO: check final frezy
                    if(currPlayer.equals(model.getCurrentTurn().getCurrentPlayer())){
                        state = ServerState.WAITING_ACTION;
                        return new ChooseTurnActionRequest();
                    }
                    else
                        return new OperationCompletedResponse("Wait for you turn..");
                }
                else {
                    state = ServerState.WAITING_TURN;
                    clientHandler.sendMessage(new RemoveSpawnPowerUp(clientMsg.getPowerUp()));
                    return new OperationCompletedResponse("You are respawned, wait for your turn!");
                }
            } else
                return new InvalidPowerUpResponse();
        }
        else{
            return new InvalidMessageResponse("You are not dead, you can't respawn!");
        }
    }

    @Override
    public ServerMessage handle(ShootActionRequest clientMsg) {
        List<CardWeapon> myWeapons = null;
        List<CardWeapon> tmp = null;
        baseDone = false;
        isOrdered = false;
        plusBeforeBase = null;
        nSimpleEffect = 0;
        FullEffect tmpEff;
        boolean check;
        /*for(CardWeapon cw : myWeapons){
            tmpEff = cw.getBaseEffect();
            check = true;
            for(SimpleEffect sE : tmpEff.getSimpleEffects())
                if()
        }*/
        if(model.getCurrentTurn().applyStep(Action.SHOOT)) {
            myWeapons = currPlayer.getWeapons();
            tmp = new ArrayList<>();
            for(CardWeapon cw : myWeapons) {
                if (!cw.isLoaded()) {
                    tmp.add(cw);
                } else {
                    currPlayer.setActualWeapon(cw);
                    if (!simulateApplication(cw))
                        tmp.add(cw);
                }
            }
            myWeapons.removeAll(tmp);
            if (myWeapons.isEmpty()) {
                clientHandler.sendMessage(new InvalidActionResponse());
                return checkTurnEnd();
            }
            state = ServerState.HANDLING_SHOOT;
            return new ChooseWeaponToShootRequest(myWeapons);
        }
        else
            return new InvalidActionResponse();
        /*CardWeapon w = currPlayer.getWeapons().stream().filter(wp -> wp.getId() == clientMsg.getWeapon().getId()).findFirst().orElse(null);

        if( (clientMsg.getBaseEffect() != null && clientMsg.getAltEffect() != null))
            return new InvalidWeaponResponse();
        if(clientMsg.getBaseEffect() != null && !clientMsg.getBaseEffect().equals(w.getBaseEffect()))
            return new InvalidWeaponResponse();
        if(clientMsg.getAltEffect() != null && !clientMsg.getAltEffect().equals((w.getAltEffect())))
            return new InvalidWeaponResponse();
        if(!w.getPlusEffects().containsAll(clientMsg.getPlusEffects()))
            return new InvalidWeaponResponse();
        if(!w.isPlusBeforeBase() && clientMsg.isPlusBeforeBase())
            return new InvalidWeaponResponse();
        if(w.isPlusOrder())
        {
            for(int i=0;i<clientMsg.getPlusEffects().size();i++)
                if(!clientMsg.getPlusEffects().get(i).equals(w.getPlusEffects().get(i)))
                    return new InvalidWeaponResponse();
        }

        List<Color> totalAmmo = new ArrayList<>();
        for(FullEffect fe : clientMsg.getPlusEffects())
            totalAmmo.addAll(fe.getPrice());
        if(clientMsg.getAltEffect() != null)
            totalAmmo.addAll(clientMsg.getAltEffect().getPrice());

        try{
            currPlayer.pay(totalAmmo,clientMsg.getPaymentWithPowerUp());
        }catch (InsufficientAmmoException ex)
        {
            return new InvalidWeaponResponse();
        }

        selectedWeapon = w;
        selectedPlusEffects = clientMsg.getPlusEffects();
        nSimpleEffect = 0;
        baseAltEffect = (clientMsg.getBaseEffect() != null) ? clientMsg.getBaseEffect() : clientMsg.getAltEffect();
        if(clientMsg.isPlusBeforeBase())
        {
            baseDone = false;
            currFullEffect = selectedPlusEffects.stream().filter(FullEffect::isBeforeBase).findFirst().orElse(selectedPlusEffects.get(0));
            selectedPlusEffects.remove(currFullEffect);
        }
        else
        {
            baseDone = true;
            currFullEffect = baseAltEffect;

        }
        return handleEffect(currFullEffect.getSimpleEffects().get(nSimpleEffect));*/
    }

    private boolean simulateApplication(CardWeapon cw) {
        if(cw.getBaseEffect().getSimpleEffects().get(0).searchTarget(currPlayer).isEmpty())
            return false;
        else if(cw.getAltEffect() != null) {
            if (!cw.getAltEffect().getSimpleEffects().get(0).searchTarget(currPlayer).isEmpty())
                return true;
            return false;
        }
        return true;
    }

    /**
     * Return existing waiting rooms to the client
     * @param getWaitingRoomsRequest
     * @return
     */
    @Override
    public ServerMessage handle(GetWaitingRoomsRequest getWaitingRoomsRequest) {
        return new WaitingRoomsListResponse(GameManager.get().getWaitingRooms());
    }

    /**
     * Join the client in a WaitingRoom
     * @param joinWaitingRoomRequest
     * @return
     */
    @Override
    public ServerMessage handle(JoinWaitingRoomRequest joinWaitingRoomRequest) {
        WaitingRoom w = GameManager.get().getWaitingRoom(joinWaitingRoomRequest.getRoomId());
        int n = 0;
        if(w != null)
        {
            n = w.addWaitingPlayer(this, joinWaitingRoomRequest.getNickName());
            if(n != w.getNumWaitingPlayers())
            {
                return new JoinWaitingRoomResponse(n,w);
            }
            else{
                for(ServerController sc : w.getServerControllers())
                {
                    sc.getCurrPlayer().setSerializeEverything(true);
                    if(sc != this){

                        sc.getClientHandler().sendMessage(new NotifyGameStarted(sc.getModel().getPlayers(),sc.getModel().getMap()));
                    }
                    else{
                        sc.getClientHandler().sendMessage(new NotifyGameStarted(sc.getModel().getMap(),sc.getModel().getPlayers(),n));

                    }

                }

                for(ServerController sc : w.getServerControllers())
                {
                    if(sc!=this)
                    {
                        if(model.getCurrentTurn().getCurrentPlayer() == sc.getCurrPlayer())
                        {
                            CardPower cp = model.drawPowerUp();
                            sc.getCurrPlayer().addCardPower(cp);
                            sc.getClientHandler().sendMessage(new OperationCompletedResponse("Game has started!"));
                            sc.getClientHandler().sendMessage(new RespawnRequest(cp));
                            model.getCurrentTurn().startTimer();

                        }
                        else
                            sc.getClientHandler().sendMessage(new OperationCompletedResponse("Game has started!\nWait your turn.."));
                    }


                }


                if(model.getCurrentTurn().getCurrentPlayer() == getCurrPlayer())
                {
                    CardPower cp = model.drawPowerUp();
                    getCurrPlayer().addCardPower(cp);
                    getClientHandler().sendMessage(new OperationCompletedResponse("Game has started!"));
                    return new RespawnRequest(cp);
                }

                return new OperationCompletedResponse("Game has started!\nWait your turn..");

            }

        }

        return new InvalidMessageResponse("The indicated room id does not exist on the Server");
    }

    @Override
    public ServerMessage handle(CreateWaitingRoomRequest createWaitingRoomRequest) {
        WaitingRoom w = GameManager.get().addWaitingRoom(createWaitingRoomRequest.getMapId(),createWaitingRoomRequest.getNumWaitingPlayers());
        int n=w.addWaitingPlayer(this, createWaitingRoomRequest.getCreatorNicknme());
        return new CreateWaitingRoomResponse(n);
    }

    @Override
    public ServerMessage handle(EndTurnRequest endTurnRequest) {
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");
        if(model.getCurrentTurn().getNumOfMovs() > 0 && model.getCurrentTurn().getCurrentAction().equals(Action.MOVEMENT))
            return checkTurnEnd();
        clientHandler.sendMessage(new OperationCompletedResponse("You can't stop the action!"));
        return new ChooseSingleActionRequest(avaialableActionSteps);
    }

    @Override
    public ServerMessage handle(GetAvailableMapsRequest getAvailableMapsRequest) {
        return new AvailableMapsListResponse(Game.getAvailableMaps());
    }

    @Override
    public ServerMessage handle(ChoosePowerUpResponse choosePowerUpResponse) {
        List<CardPower> avPowerUp = currPlayer.getCardPower();
        CardPower cp = choosePowerUpResponse.getCardPower();
        if(state == ServerState.WAITING_ACTION)
            avPowerUp = avPowerUp.stream().filter(c -> !c.isUseWhenDamaged() && !c.isUseWhenAttacking()).collect(Collectors.toList());

        if(!avPowerUp.contains(cp))
            return new InvalidPowerUpResponse();

        currFullEffect = cp.getEffect();
        currSimpleEffect = currFullEffect.getSimpleEffects().get(0);
        nSimpleEffect = 0;

        baseDone = true; //TODO: check

        return handleEffect(currSimpleEffect);

    }

    @Override
    public ServerMessage handle(LoginMessage loginMessage) {
        if(GameManager.get().getUsersLogged().contains(loginMessage.getNickname()))
            return new UserAlreadyLoggedResponse();

        this.nickname = loginMessage.getNickname();
        if(GameManager.get().getUsersSuspended().contains(loginMessage.getNickname()))
        {
            model = GameManager.get().getNameOfSuspendedUser(loginMessage.getNickname());
            List<String> otherPlayerNames = model.getMap().getAllPlayers().stream().filter(p -> !p.isSuspended()).map(Player::getNickName).collect(Collectors.toList());

            return new RejoinGameRequest(otherPlayerNames);
        }

        GameManager.get().addLoggedUser(loginMessage.getNickname());
        return new UserLoggedResponse();
    }

    /**
     * Handle the rejoin game response, rejoining the user to its old game if he accepted, otherwise simply logging it on the server
     * @param rejoinGameResponse
     * @return
     */
    @Override
    public ServerMessage handle(RejoinGameResponse rejoinGameResponse) {
        boolean rejoin = rejoinGameResponse.isRejoin();
        if(rejoin)
        {
            int id = 0;
            model.addGameListener(this.clientHandler);
            Player p = model.getPlayers().stream().filter(pl -> pl.getNickName().equals(rejoinGameResponse.getUser())).findFirst().orElse(null);
            if(p!=null){
                this.currPlayer = p;
                p.setPlayerObserver(this);
                p.setSerializeEverything(true);
                id = p.getId();
            }
            if(p.getPosition() != null)
                this.state = ServerState.WAITING_TURN;
            else
                this.state = ServerState.WAITING_SPAWN;
            GameManager.get().rejoinUser(rejoinGameResponse.getUser());
            return new RejoinGameConfirm(model.getMap(),model.getPlayers(),id);
        }
        else
        {
            GameManager.get().addLoggedUser(rejoinGameResponse.getUser());
            return new UserLoggedResponse();
        }
    }

    @Override
    public ServerMessage handle(ChooseWeaponToShootResponse chooseWeaponToShootResponse) {
        selectedWeapon = chooseWeaponToShootResponse.getSelectedWeapon();
        currPlayer.setActualWeapon(selectedWeapon);
        boolean correct = false;
        if( selectedWeapon == null)
            return new InvalidWeaponResponse();
        if( !selectedWeapon.isLoaded())
            return new InvalidWeaponResponse();
        for(CardWeapon cw : currPlayer.getWeapons())
            if(cw.equals(selectedWeapon))
                correct = true;
        if(!correct)
            return new InvalidWeaponResponse();
        FullEffect plusEff = null;
        if(selectedWeapon.getPlusEffects() != null) {
            for (FullEffect pE : selectedWeapon.getPlusEffects())
                if (pE.isBeforeBase())
                    plusEff = pE;
            if (plusEff != null)
                return new BeforeBaseRequest(plusEff);
        }
        return firstEffect();
    }

    private ServerMessage firstEffect(){
        FullEffect baseEff = selectedWeapon.getBaseEffect();
        FullEffect altEff = null;
        if (selectedWeapon.getAltEffect() != null) {
            if(currPlayer.canUsetWeaponEffect(selectedWeapon.getAltEffect())) {
                altEff = selectedWeapon.getAltEffect();
                return new ChooseFirstEffectRequest(baseEff, altEff);
            }
        }
        currFullEffect = baseEff;
        currSimpleEffect = currFullEffect.getSimpleEffect(0);
        baseDone = true;
        return handleEffect(currSimpleEffect);
        //return managePlusEffectChoice();
    }

    private ServerMessage managePlusEffectChoice() {
        if (selectedWeapon.getPlusEffects() != null){
            if(remainingPlusEffects == null)
                remainingPlusEffects = new ArrayList<>(selectedWeapon.getPlusEffects());
            if(plusBeforeBase != null) {
                for (FullEffect fe : remainingPlusEffects)
                    if (fe.equals(plusBeforeBase))
                        remainingPlusEffects.remove(fe);
            }
            if (selectedWeapon.isPlusOrder() && currPlayer.canUsetWeaponEffect(remainingPlusEffects.get(0))) {
                isOrdered = true; //maybe not necessary
                return new UsePlusByOrderRequest(remainingPlusEffects);
            }
            else {
                for (FullEffect fe : remainingPlusEffects)
                    if (!currPlayer.canUsetWeaponEffect(fe))
                        remainingPlusEffects.remove(fe);
                if (!remainingPlusEffects.isEmpty())
                    return new UsePlusEffectRequest(remainingPlusEffects);
                else
                    //TODO
                    return new OperationCompletedResponse();
            }
        }
        else
            return terminateFullEffect();
    }

    @Override
    public ServerMessage handle(ChooseFirstEffectResponse chooseFirstEffectResponse) throws InsufficientAmmoException {
        if(chooseFirstEffectResponse.getN() == 1)
            currFullEffect = selectedWeapon.getBaseEffect();
        else {
            addFinalPayment(currFullEffect.getPrice(),chooseFirstEffectResponse.getToUse());
            currFullEffect = selectedWeapon.getAltEffect();
        }
        currSimpleEffect = currFullEffect.getSimpleEffect(0);
        baseDone = true;
        return handleEffect(currSimpleEffect);
        //return managePlusEffectChoice();
    }

    @Override
    public ServerMessage handle(UsePlusBeforeResponse usePlusBeforeResponse) throws InsufficientAmmoException {
        if(usePlusBeforeResponse.getT() == 'Y' || usePlusBeforeResponse.getT() == 'y') {
            plusBeforeBase = usePlusBeforeResponse.getPlusEff();
            currFullEffect = plusBeforeBase;
            currSimpleEffect = currFullEffect.getSimpleEffect(0);
            addFinalPayment(currFullEffect.getPrice(),usePlusBeforeResponse.getToUse());
            return handleEffect(currSimpleEffect);
        }
        return firstEffect();
    }

    private void addFinalPayment(List<Color> price, List<CardPower> toUse) throws InsufficientAmmoException {
        if(price != null) {
            if (ammoToPay == null)
                ammoToPay = new ArrayList<>(price);
            else
                ammoToPay.addAll(price);
        }
        if(toUse != null) {
            if (powerUpToPay == null)
                powerUpToPay = new ArrayList<>(toUse);
            else
                powerUpToPay.addAll(toUse);
        }
        currPlayer.pay(currFullEffect.getPrice(),toUse);
    }

    @Override
    public ServerMessage handle(UseOrderPlusResponse useOrderPlusResponse) throws InsufficientAmmoException {
        if(useOrderPlusResponse.getT() == 'Y' || useOrderPlusResponse.getT() == 'y') {
            remainingPlusEffects = remainingPlusEffects.subList(1,remainingPlusEffects.size());
            currFullEffect = useOrderPlusResponse.getPlusEffects().get(0);
            currSimpleEffect = currFullEffect.getSimpleEffect(0);
            addFinalPayment(currFullEffect.getPrice(),useOrderPlusResponse.getToUse());
            return handleEffect(currSimpleEffect);
        }
        else
            return terminateFullEffect();

    }

    private ServerMessage terminateFullEffect() {
        if(currFullEffect != null && nSimpleEffect < currFullEffect.getSimpleEffects().size())
            return handleEffect(currFullEffect.getSimpleEffects().get(nSimpleEffect));
        selectedWeapon.setLoaded(false);
        clientHandler.sendMessage(new ShootActionResponse(selectedWeapon,ammoToPay,powerUpToPay));
        return checkShootActionEnd();
    }

    @Override
    public ServerMessage handle(UsePlusEffectResponse usePlusEffectResponse) throws InsufficientAmmoException {
        remainingPlusEffects = new ArrayList<>(usePlusEffectResponse.getPlusRemained());
        currFullEffect = usePlusEffectResponse.getEffectToApply();
        currSimpleEffect = currFullEffect.getSimpleEffect(0);
        addFinalPayment(currFullEffect.getPrice(),usePlusEffectResponse.getToUse());
        return handleEffect(currSimpleEffect);
    }

    @Override
    public ServerMessage handle(TerminateShootAction terminateShootAction) {
        return terminateFullEffect();
    }


    /**
     * Ask the user for the targets of the next simpleEffect, if there is the need of a choice
     * If the choice is not needed, it passes to the next simpleEffect
     * When simpleEffects are terminated it passes to the next FullEffect
     * When fullEffects are terminated it return a confirm message
     * It's overloaded for MovementEffect
     * @return
     */
    private ServerMessage handleEffect(SimpleEffect e)
    {
        currSimpleEffect = e;
        if(nSimpleEffect == currFullEffect.getSimpleEffects().size())
        {
            nSimpleEffect=0;
            if(!baseDone)
                return firstEffect();
            else if(!remainingPlusEffects.isEmpty())
                return managePlusEffectChoice();
            return checkTurnEnd();

        }
        else
            nSimpleEffect++;
        selectableTarget = e.searchTarget(currPlayer);
        if(selectableTarget.isEmpty())
            return new InvalidWeaponResponse();

        if(selectableTarget.size() > e.getMaxEnemy() || (e.getMinEnemy() < e.getMaxEnemy() && selectableTarget.size() > e.getMinEnemy()))
            return new ChooseTargetRequest(selectableTarget,currSimpleEffect);

        //apply without asking nothing if it is not necessary
        e.applyEffect(currPlayer,selectableTarget);
        return checkShootActionEnd();
    }

    /**
     * Ask the user for the targets of the next MovementEffect, if there is the need of a choice
     * If the choice is not needed, it asks for the square where the target has to be moved, if it is needed
     * If no choice is needed it passes to the next SingleEffect
     * When simpleEffects are terminated it passes to the next FullEffect
     * When fullEffects are terminated it return a confirm message
     * @return
     */
    private ServerMessage handleEffect(MovementEffect e)
    {
        currSimpleEffect = e;
        if(nSimpleEffect == currFullEffect.getSimpleEffects().size())
        {
            nSimpleEffect=0;
            //setNextFullEffect();
        }
        else
            nSimpleEffect++;

        if(!e.isMoveShooter())
        {
            selectableTarget = e.searchTarget(currPlayer);
            if(selectableTarget.isEmpty())
                return new InvalidWeaponResponse();
            if(selectableTarget.size() > e.getMaxEnemy() || (e.getMinEnemy() < e.getMaxEnemy() && selectableTarget.size() > e.getMinEnemy()))
                return new ChooseTargetRequest(selectableTarget, currSimpleEffect);
        }
        else{
            selectableTarget = Collections.singletonList(currPlayer);
        }


        selectableSquares = e.selectPosition(currPlayer);
        if(selectableSquares.isEmpty())
            return new InvalidWeaponResponse();
        if(selectableSquares.size() > 1)
            return new ChooseSquareRequest(selectableSquares);

        //apply without asking nothing if it is not necessary
        e.applyEffect((Player)selectableTarget.get(0),Collections.singletonList(selectableSquares.get(0)));
        if(currFullEffect != null)
            return handleEffect(currFullEffect.getSimpleEffects().get(nSimpleEffect));

        return new OperationCompletedResponse();
    }


    /*
     * Sets the next FullEffect in the list of requested effects, when they are finished it becomes null
     *
    private void setNextFullEffect(){
        if(!baseDone)
            currFullEffect = baseAltEffect;
        else
        {
            if(!selectedPlusEffects.isEmpty())
            {
                currFullEffect = selectedPlusEffects.get(0);
                selectedPlusEffects.remove(currFullEffect);
            }
            else
                currFullEffect = null;
        }
    }*/


    /**
     * Ask a player to respawn, sending him the drawn powerup card
     */
    @Override
    public void onRespawn() {
        state = ServerState.WAITING_RESPAWN;
        clientHandler.sendMessage(new RespawnRequest(model.drawPowerUp()));
    }

    @Override
    public void onTurnStart() {
        if(state != ServerState.WAITING_SPAWN)
        {
            state = ServerState.WAITING_ACTION;
            clientHandler.sendMessage(new ChooseTurnActionRequest());
        }
        else{
            CardPower cp = model.drawPowerUp();
            currPlayer.addCardPower(cp);
            clientHandler.sendMessage(new RespawnRequest(cp));
        }

    }

    /**
     * When the player is suspended from the game end the current turn if it's his turn and eventually notify him if the suspension is caused by a timeout (and not by a connection error)
     * @param timeOut
     */
    @Override
    public void onSuspend(boolean timeOut) {

        if(model.getCurrentTurn().getCurrentPlayer().equals(this.currPlayer) && !model.isEnded())
            endTurnManagement();
        if(timeOut)
            clientHandler.sendMessage(new TimeOutNotify());
    }

    void notifyPlayerExitedFromWaitingRoom(int pId){
        clientHandler.sendMessage(new NotifyPlayerExitedWaitingRoom(pId));
    }

    void leaveWaitingRoom() {
        waitingRoom.removeWaitingPlayer(this);
    }

    void notifyPlayerJoinedWaitingRoom(Player p)
    {
        clientHandler.sendMessage(new NotifyPlayerJoinedWaitingRoom(p));
    }


}
