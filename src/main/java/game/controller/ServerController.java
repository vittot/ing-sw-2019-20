package game.controller;

import game.controller.commands.ClientGameMessageHandler;
import game.controller.commands.ServerGameMessage;
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

public class ServerController implements ClientGameMessageHandler, PlayerObserver, EffectHandler {

    private ClientHandler clientHandler;

    private final GameManager gameManager;

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
    private boolean damageEffect;
    private WaitingRoom waitingRoom;
    private String nickname;


    public ServerController(ClientHandler clientHandler) {

        this.clientHandler = clientHandler;
        this.state = ServerState.JUST_LOGGED;
        this.gameManager = GameManager.get();
    }

    public ServerController(ClientHandler clientHandler, GameManager gameManager)
    {
        this.clientHandler = clientHandler;
        this.state = ServerState.JUST_LOGGED;
        this.gameManager = gameManager;
    }

    public ServerController(){
        this.gameManager = GameManager.get();
    }

    public ClientHandler getClientHandler() {
        return clientHandler;
    }

    public ServerState getState() {
        return state;
    }

    public void setState(ServerState state) {
        this.state = state;
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

    public ServerGameMessage handle(CheckValidWeaponRequest clientMsg){
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());

        List<CardWeapon> cardWeapon = new ArrayList<>();
        for(CardWeapon cw : currPlayer.getWeapons()){
            if(cw.checkweapon(clientMsg.getPlayer())){
                cardWeapon.add(cw);
                System.out.println("added" + cw.toString());
            }
        }
        return new OperationCompletedResponse("");
    }

    @Override
    public ServerGameMessage handle(ChooseSquareToShootResponse chooseSquareToShootResponse) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

        List<Square> selectedTargets = chooseSquareToShootResponse.getChoosenSquare();
        List<Target> toApplyEffect = null;
        if(!selectableTarget.containsAll(selectedTargets)) {
            clientHandler.sendMessage(new InvalidTargetResponse());
            return checkShootActionEnd();
        }

        if(selectedTargets.isEmpty())
            return new OperationCompletedResponse("");
        toApplyEffect = new ArrayList<>();
        for(Square t : model.getMap().getAllSquares())
            if(selectedTargets.contains(t))
                toApplyEffect.addAll(t.getPlayers());
        return currSimpleEffect.handleTargetSelection(this, toApplyEffect, model);
    }

    @Override
    public ServerGameMessage handle(CounterAttackResponse counterAttackResponse) {
        if(counterAttackResponse.isConfirm()){
            if(getCurrPlayer().getCardPower().contains(counterAttackResponse.getCardPower())) {
                model.getMap().getPlayerById(counterAttackResponse.getToShoot().getId()).addThisTurnMarks(getCurrPlayer(), 1);
                getCurrPlayer().removePowerUp(Collections.singletonList(counterAttackResponse.getCardPower()));
                return new ChoosePowerUpUsed(counterAttackResponse.getCardPower());
            }
            return new InvalidPowerUpResponse();
        }
        return new OperationCompletedResponse("");
    }

    @Override
    public ServerGameMessage handle(LogoutRequest logoutRequest) {
        GameManager.get().removeLoggedUser(logoutRequest.getUsername());
        return new OperationCompletedResponse("Logged out successfully!");
    }


    public Player getCurrPlayer() {
        return currPlayer;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public ServerGameMessage handle(ChooseSquareResponse clientMsg) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

        Square selectedSquare;
        try{
            selectedSquare = model.getMap().getSquare(clientMsg.getSelectedSquare().getX(),clientMsg.getSelectedSquare().getY());
        }catch(MapOutOfLimitException e)
        {
            //TODO: ??
            clientHandler.sendMessage(new InvalidTargetResponse());
            return checkTurnEnd();
        }
        boolean correctSquare = false;
        for(Square s : selectableSquares)
            if(s.equals(selectedSquare))
                correctSquare = true;
        if(!correctSquare) {
            clientHandler.sendMessage(new InvalidTargetResponse());
            return checkTurnEnd();
        }

        Square start = toBeMoved.getPosition();
        currSimpleEffect.applyEffect(toBeMoved,Collections.singletonList(selectedSquare));

        //return new NotifyMovement(currPlayer.getId(),clientMsg.getSelectedSquare().getX(),clientMsg.getSelectedSquare().getY());
        if(state  == ServerState.HANDLING_MOVEMENT) {
            if (!avaialableActionSteps.isEmpty())
                return new ChooseSingleActionRequest(avaialableActionSteps);
            else {
                return checkTurnEnd();
            }
        }
        else if(state == ServerState.WAITING_POWER_USAGE){
            if(currFullEffect.getSimpleEffects().size() > nSimpleEffect) {
                Square dest = toBeMoved.getPosition();
                currPlayer.getActualCardPower().setLastDirection(GameMap.getDirection(start,dest));
                currPlayer.getActualCardPower().setLastTarget(toBeMoved);
                return terminateFullEffect();
            }
            return checkTurnEnd();
        }
        else {
            CardWeapon cw = currPlayer.getActualWeapon();
            List<Player> prev = cw.getPreviousTargets();
            prev.remove(toBeMoved);
            prev.add(toBeMoved);
            Square dest = toBeMoved.getPosition();
            currPlayer.getActualWeapon().setLastDirection(GameMap.getDirection(start,dest));
            return terminateFullEffect();
        }

    }

    void endTurnManagement()
    {
        //state
        model.refillMap();
        model.getCurrentTurn().stopTimer();
        List<Player> toBeRespawned = model.changeTurn();
        if(!model.getThisTurnKill().isEmpty())
            model.addKill();
        toBeRespawned.forEach(Player::notifyRespawn);
        if(model.getnPlayerToBeRespawned() == 0)
            model.getCurrentTurn().newTurn(false); //TODO: check final frezy
    }

    /**
     * Validate the selected target for an effect and apply it, or ask for the movement position if it's a MovementEffect
     * @param clientMsg
     * @return
     */
    @Override
    public ServerGameMessage handle(ChooseTargetResponse clientMsg)
    {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

        if(!clientMsg.getSelectedTargets().isEmpty()) {
            List<Target> selectedTargets = clientMsg.getSelectedTargets();
            if (!selectableTarget.containsAll(selectedTargets)) {
                clientHandler.sendMessage(new InvalidTargetResponse());
                return checkShootActionEnd();
            }
            if (!validateSelectedTargets(selectedTargets, currSimpleEffect)) {
                clientHandler.sendMessage(new InvalidTargetResponse());
                return checkShootActionEnd();
            }
            //in case no target has been selected (and it's allowed) it stops
            if (selectedTargets.isEmpty())
                return new OperationCompletedResponse("");
            return currSimpleEffect.handleTargetSelection(this, selectedTargets, model);
        }
        else
            return terminateFullEffect();
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

    private ServerGameMessage handleTargetSelectionMovement(List<Target> selectedTarget) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

        MovementEffect s = (MovementEffect) currSimpleEffect;

        if(selectableSquares != null) {
            if (selectableSquares.isEmpty()) {
                clientHandler.sendMessage(new InvalidWeaponResponse());
                return checkShootActionEnd();
            }
        }

        toBeMoved = (Player) selectedTarget.get(0);
        selectableSquares = s.selectPosition(toBeMoved);
        Square before = toBeMoved.getPosition();

        if (selectableSquares.isEmpty()) {
            clientHandler.sendMessage(new InvalidWeaponResponse());
            return checkShootActionEnd();
        }

        if (selectableSquares.size() > 1)
            return new ChooseSquareRequest(selectableSquares);

        //apply without asking nothing if it is not necessary

        s.applyEffect(toBeMoved, Collections.singletonList(selectableSquares.get(0)));
        if(state == ServerState.WAITING_POWER_USAGE) {
            state = ServerState.WAITING_POWER_TERMINATE;
            return terminateFullEffect();
        }
        List<Player> prevTargets = currPlayer.getActualWeapon().getPreviousTargets();
        Square after = toBeMoved.getPosition();
        prevTargets.remove(toBeMoved);
        prevTargets.add(toBeMoved);
        currPlayer.getActualWeapon().setLastDirection(GameMap.getDirection(before,after));
        if (currFullEffect != null && nSimpleEffect < currFullEffect.getSimpleEffects().size())
            return currFullEffect.getSimpleEffects().get(nSimpleEffect).handle(this);
        else
            return terminateFullEffect();
    }

    /**
     * Apply a simpleEffect and manage the next simple effect, if there is any
     * @param selectedTarget
     * @return
     */
    private ServerGameMessage handleTargetSelection(List<Target> selectedTarget) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

        if(selectedTarget.isEmpty())
            clientHandler.sendMessage(new InvalidTargetResponse());
        else
            currSimpleEffect.applyEffect(currPlayer, selectedTarget);
        return terminateFullEffect();
    }

    private ServerGameMessage checkShootActionEnd(){
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());

        if(state == ServerState.WAITING_POWER_USAGE){
            currPlayer.setActualCardPower(null);
            return checkTurnEnd();
        }

        damageEffect = false;
        if(!baseDone)
            return firstEffect();
        else if(remainingPlusEffects != null) {
            if (!remainingPlusEffects.isEmpty())
                return managePlusEffectChoice();
        }
        if(baseDone)
            selectedWeapon.setLoaded(false);
        if(remainingPlusEffects != null)
            remainingPlusEffects.clear();
        selectedWeapon.getPreviousTargets().clear();
        currPlayer.setActualWeapon(null);
        clientHandler.sendMessage(new ShootActionResponse(selectedWeapon));
        return checkTurnEnd();
    }

    @Override
    public ServerGameMessage handle(ChooseTurnActionResponse clientMsg) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

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

    /**
     * Check if the game is already ended
     * @return
     */
    private boolean checkIfEnded() {
        return (this.state == ServerState.GAME_ENDED);
    }

    @Override
    public ServerGameMessage handle(GrabActionRequest clientMsg) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

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
                listA = currPlayer.controlGrabAmmo(listA);
                List<CardPower> listCp;
                try {
                    listCp = new ArrayList<>(currPlayer.pickUpAmmo());
                }catch(NoCardAmmoAvailableException e)
                {
                    //TODO: send an error message?
                    return new InvalidGrabPositionResponse();
                }
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
    public ServerGameMessage handle(MovementActionRequest clientMsg) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

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
    public ServerGameMessage handle(PickUpAmmoRequest clientMsg) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

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
    public ServerGameMessage handle(PickUpWeaponRequest clientMsg) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

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
            avaialableActionSteps = model.getCurrentTurn().getActionList();
            return checkTurnEnd();
        }
        catch (NoCardWeaponSpaceException x){
            clientHandler.sendMessage(new MaxNumberOfWeaponsResponse());
            avaialableActionSteps = model.getCurrentTurn().getActionList();
            return checkTurnEnd();
        }
    }

    private ServerGameMessage checkTurnEnd()
    {
        List<CardWeapon> weaponsToReload;
        if (model.getCurrentTurn().getNumOfActions() > 0) {
            state = ServerState.WAITING_ACTION;
            model.getPlayers().forEach(Player::updateMarks);
            return new ChooseTurnActionRequest();
        } else {
            if (!currPlayer.getWeapons().isEmpty() && state != ServerState.WAITING_RELOAD) {
                weaponsToReload = currPlayer.hasToReload();
                if(weaponsToReload != null)
                    for(CardWeapon cw : currPlayer.getWeapons())
                        if(!currPlayer.canReloadWeapon(cw))
                            weaponsToReload.remove(cw);
                if (weaponsToReload != null && !weaponsToReload.isEmpty()) {
                    state = ServerState.WAITING_RELOAD;
                    return new ReloadWeaponAsk(weaponsToReload);
                }
            }

            endTurnManagement();

            if(currPlayer.equals(model.getCurrentTurn().getCurrentPlayer())){
                state = ServerState.WAITING_ACTION;
                return new ChooseTurnActionRequest();
            }
            else {
                state = ServerState.WAITING_TURN;
                return new OperationCompletedResponse("Wait for you next turn!");
            }
        }
    }

    @Override
    public ServerGameMessage handle(EndTurnRequest endTurnRequest) {
        return checkTurnEnd();
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
    public ServerGameMessage handle(ReloadWeaponRequest clientMsg) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

        CardWeapon w = currPlayer.getWeapons().stream().filter(wp -> wp.getId() == clientMsg.getWeapon().getId()).findFirst().orElse(null);
        int count = 0;
        List<CardPower> tmp;
        if( w == null)
            return new InvalidWeaponResponse();
        if( w.isLoaded())
            return new InvalidWeaponResponse();
        if(clientMsg.getPowerups() != null) {
            tmp = new ArrayList<>(clientMsg.getPowerups());
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
            state = ServerState.WAITING_RELOAD;
            clientHandler.sendMessage(new CheckReloadResponse(clientMsg.getWeapon(), clientMsg.getPowerups()));
            return checkTurnEnd();
        }catch(InsufficientAmmoException e)
        {
            clientHandler.sendMessage(new InsufficientAmmoResponse());
            state = ServerState.WAITING_RELOAD;
            return checkTurnEnd();
        }
    }

    /*
    @Override
    public ServerGameMessage handle(SpawnResponse clientMsg){
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
    public ServerGameMessage handle(RespawnResponse clientMsg) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer && state != ServerState.WAITING_RESPAWN)
            return new OperationCompletedResponse("Not your turn!");

        if (currPlayer.isDead() || state == ServerState.WAITING_SPAWN) {
            if (currPlayer.getCardPower().contains(clientMsg.getPowerUp())) {
                currPlayer.respawn(clientMsg.getPowerUp());
                currPlayer.removePowerUp(Collections.singletonList(clientMsg.getPowerUp()));
                model.addPowerWaste(clientMsg.getPowerUp());
                if (state == ServerState.WAITING_SPAWN && model.getCurrentTurn().getCurrentPlayer().equals(currPlayer)) {
                    state = ServerState.WAITING_ACTION;
                    /*model.getCurrentTurn().stopTimer();
                    model.getCurrentTurn().startTimer();*/
                    clientHandler.sendMessage(new RemoveSpawnPowerUp(clientMsg.getPowerUp()));
                    return new ChooseTurnActionRequest();
                }
                else if(state == ServerState.WAITING_RESPAWN)
                {
                    state = ServerState.WAITING_TURN;
                    model.decreaseToBeRespawned();
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

    /**
     * the server receive the shoot action request and ask the player which weapons from the available want to use
     * @param clientMsg
     * @return
     */
    @Override
    public ServerGameMessage handle(ShootActionRequest clientMsg) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

        List<CardWeapon> myWeapons = null;
        List<CardWeapon> tmp = null;
        baseDone = false;
        isOrdered = false;
        plusBeforeBase = null;
        nSimpleEffect = 0;
        damageEffect = true;
        if(remainingPlusEffects != null)
            remainingPlusEffects.clear();
        FullEffect tmpEff;
        boolean check;

        //weapon selection request
        if(model.getCurrentTurn().applyStep(Action.SHOOT)) {
            myWeapons = new ArrayList<>(currPlayer.getWeapons());
            tmp = new ArrayList<>();
            for(CardWeapon cw : myWeapons) {
                if (!cw.isLoaded()) {
                    tmp.add(cw);
                } else {
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
        else{
            clientHandler.sendMessage(new InvalidActionResponse());
            return checkTurnEnd();
        }
    }

    private boolean simulateApplication(CardWeapon cw) {
        if(!cw.getBaseEffect().getSimpleEffects().get(0).searchTarget(currPlayer).isEmpty())
            return true;
        else if(cw.getAltEffect() != null) {
            if (!cw.getAltEffect().getSimpleEffects().get(0).searchTarget(currPlayer).isEmpty())
                return true;
        }
        else if(cw.getPlusEffects() != null){
            for(FullEffect fe : cw.getPlusEffects())
                if(fe.isBeforeBase())
                    if(!fe.getSimpleEffects().get(0).searchTarget(currPlayer).isEmpty())
                        return true;
        }
        return false;
    }

    /**
     * Return existing waiting rooms to the client
     * @param getWaitingRoomsRequest
     * @return
     */
    @Override
    public ServerGameMessage handle(GetWaitingRoomsRequest getWaitingRoomsRequest) {
        return new WaitingRoomsListResponse(gameManager.getWaitingRooms());
    }

    /**
     * Join the client in a WaitingRoom
     * @param joinWaitingRoomRequest
     * @return
     */
    @Override
    public ServerGameMessage handle(JoinWaitingRoomRequest joinWaitingRoomRequest) {
        WaitingRoom w = gameManager.getWaitingRoom(joinWaitingRoomRequest.getRoomId());

        if(w != null) {
            int n;
            n = w.addWaitingPlayer(this, joinWaitingRoomRequest.getNickName());
            //if (n != w.getNumWaitingPlayers()) {
                return new JoinWaitingRoomResponse(n, w);
            /*} else {

            }*/
        }

        return new InvalidMessageResponse("The indicated room id does not exist on the Server");
    }

    /**
     * Send NotifyGameStarted and RespawnRequest
     * @param w
     */
    public void launchGame(WaitingRoom w)
    {
        for (ServerController sc : w.getServerControllers()) {
            sc.getClientHandler().sendMessage(new NotifyGameStarted(sc.getModel().getMap(), sc.getModel().getPlayers(), sc.getCurrPlayer().getId(), sc.getModel().getKillBoard()));

            if (model.getCurrentTurn().getCurrentPlayer() != sc.getCurrPlayer())
                sc.getClientHandler().sendMessage(new OperationCompletedResponse("Wait your turn.."));

        }

        for (ServerController sc : w.getServerControllers()) {
            //if (sc != this) {
                if (model.getCurrentTurn().getCurrentPlayer() == sc.getCurrPlayer()) {
                    CardPower cp = model.drawPowerUp();
                    sc.getCurrPlayer().addCardPower(cp);
                    model.getCurrentTurn().startTimer();
                    sc.getClientHandler().sendMessage(new RespawnRequest(cp));

                }

            //}
        }


        /*if (model.getCurrentTurn().getCurrentPlayer() == getCurrPlayer()) {
            CardPower cp = model.drawPowerUp();
            getCurrPlayer().addCardPower(cp);
            //getClientHandler().sendMessage(new OperationCompletedResponse("Game has started!"));
            model.getCurrentTurn().startTimer();
            getClientHandler().sendMessage(new RespawnRequest(cp));
        }*/

    }

    @Override
    public ServerGameMessage handle(CreateWaitingRoomRequest createWaitingRoomRequest) {
        WaitingRoom w = gameManager.addWaitingRoom(createWaitingRoomRequest.getMapId());
        int n=w.addWaitingPlayer(this, createWaitingRoomRequest.getCreatorNicknme());
        return new CreateWaitingRoomResponse(n);
    }

    @Override
    public ServerGameMessage handle(EndActionRequest endActionRequest) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());

        if(state == ServerState.WAITING_RELOAD) {
            return checkTurnEnd();
        }
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

        if(model.getCurrentTurn().getNumOfMovs() > 0 && model.getCurrentTurn().getCurrentAction().equals(Action.MOVEMENT))
            return checkTurnEnd();
        clientHandler.sendMessage(new OperationCompletedResponse("You can't stop the action!"));
        return new ChooseSingleActionRequest(avaialableActionSteps);
    }

    @Override
    public ServerGameMessage handle(GetAvailableMapsRequest getAvailableMapsRequest) {
        return new AvailableMapsListResponse(GameManager.get().getAvailableMaps());
    }

    @Override
    public ServerGameMessage handle(ChoosePowerUpResponse choosePowerUpResponse) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

        if(choosePowerUpResponse.isConfirm()) {
            List<CardPower> avPowerUp = currPlayer.getCardPower();
            CardPower cp = null;
            for(CardPower c : avPowerUp)
                if(c.equals(choosePowerUpResponse.getCardPower())) {
                    currPlayer.setActualCardPower(c);
                    cp = c;
                }
            if(state == ServerState.WAITING_ACTION)
                avPowerUp = avPowerUp.stream().filter(c -> !c.isUseWhenDamaged() && !c.isUseWhenAttacking()).collect(Collectors.toList());

            if (!avPowerUp.contains(cp)) {
                clientHandler.sendMessage(new InvalidPowerUpResponse());
                return checkTurnEnd();
            }
            state = ServerState.WAITING_POWER_USAGE;
            currFullEffect = cp.getEffect();
            currSimpleEffect = currFullEffect.getSimpleEffects().get(0);
            nSimpleEffect = 0;

            //baseDone = true; //TODO: check
            if(choosePowerUpResponse.getAmmoToPay() != Color.ANY) {
                try {
                    currPlayer.pay(Collections.singletonList(choosePowerUpResponse.getAmmoToPay()),new ArrayList<>());
                    clientHandler.sendMessage(new AddPayment(Collections.singletonList(choosePowerUpResponse.getAmmoToPay()),new ArrayList<>()));
                } catch (InsufficientAmmoException e) {
                    e.printStackTrace();
                }
            }
            clientHandler.sendMessage(new ChoosePowerUpUsed(cp));
            avPowerUp.remove(cp);
            return currSimpleEffect.handle(this);
        }
        return terminateFullEffect();
    }

    @Override
    public ServerGameMessage handle(LoginMessage loginMessage) {
        if(!loginMessage.isReconnecting() && gameManager.getUsersLogged().contains(loginMessage.getNickname()))
            return new UserAlreadyLoggedResponse();

        this.nickname = loginMessage.getNickname();
        if(gameManager.getUsersSuspended().contains(loginMessage.getNickname()) || loginMessage.isReconnecting())
        {
            if(gameManager.getUsersSuspended().contains(loginMessage.getNickname()))
                model = gameManager.getGameOfSuspendedUser(loginMessage.getNickname());
            else
                model = gameManager.getGameOfUser(loginMessage.getNickname());

            if (model != null) {
                List<String> otherPlayerNames = model.getMap().getAllPlayers().stream().filter(p -> !p.isSuspended()).map(Player::getNickName).collect(Collectors.toList());

                if (loginMessage.isReconnecting() && model.getCurrentTurn().getCurrentPlayer().getNickName().equals(loginMessage.getNickname()))
                    endTurnManagement();

                return new RejoinGameRequest(otherPlayerNames);
            }
        }

        gameManager.addLoggedUser(loginMessage.getNickname());
        return new UserLoggedResponse();
    }

    /**
     * Handle the rejoin game response, rejoining the user to its old game if he accepted, otherwise simply logging it on the server
     * @param rejoinGameResponse
     * @return
     */
    @Override
    public ServerGameMessage handle(RejoinGameResponse rejoinGameResponse) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        boolean rejoin = rejoinGameResponse.isRejoin();
        if(rejoin)
        {
            int id = 0;
            GameServer.get().removeHandler(rejoinGameResponse.getUser(),this.clientHandler);
            model.removeGameListener(rejoinGameResponse.getUser());
            model.addGameListener(this.clientHandler);
            Player p = model.getPlayers().stream().filter(pl -> pl.getNickName().equals(rejoinGameResponse.getUser())).findFirst().orElse(null);
            if(p!=null){
                this.currPlayer = p;
                p.setPlayerObserver(this);
                id = p.getId();
            }
            if(p.getPosition() != null)
                this.state = ServerState.WAITING_TURN;
            else
                this.state = ServerState.WAITING_SPAWN;
            gameManager.rejoinUser(rejoinGameResponse.getUser());
            this.clientHandler.startPing(Configuration.PING_INTERVAL_MS);
            return new RejoinGameConfirm(model.getMap(),model.getPlayers(),id);
        }
        else
        {
            gameManager.addLoggedUser(rejoinGameResponse.getUser());
            return new UserLoggedResponse();
        }
    }

    /**
     * the server receive the client choose about which weapon he wants to use and list him which first effect he can apply
     * @param chooseWeaponToShootResponse
     * @return
     */
    @Override
    public ServerGameMessage handle(ChooseWeaponToShootResponse chooseWeaponToShootResponse) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

        boolean correct = false;
        for(CardWeapon cw : currPlayer.getWeapons())
            if(cw.equals(chooseWeaponToShootResponse.getSelectedWeapon())) {
                correct = true;
                selectedWeapon = cw;
            }
        //verify selected weapon is valid
        if(!correct) {
            clientHandler.sendMessage(new InvalidWeaponResponse());
            return checkTurnEnd();
        }
        currPlayer.setActualWeapon(selectedWeapon);
        if( selectedWeapon == null){
            clientHandler.sendMessage(new InvalidWeaponResponse());
            return checkTurnEnd();
        }
        if( !selectedWeapon.isLoaded()){
            clientHandler.sendMessage(new InvalidWeaponResponse());
            return checkTurnEnd();
        }
        FullEffect plusEff = null;
        if(selectedWeapon.getPlusEffects() != null && !selectedWeapon.getPlusEffects().isEmpty()) {
            if(remainingPlusEffects == null)
                remainingPlusEffects = new ArrayList<>(selectedWeapon.getPlusEffects());
            else
                remainingPlusEffects.addAll(selectedWeapon.getPlusEffects());
            for (FullEffect pE : remainingPlusEffects)
                if (pE.isBeforeBase())
                    plusEff = pE;
            if (plusEff != null)
                    if(currPlayer.canUseWeaponEffect(plusEff))
                        return new BeforeBaseRequest(plusEff);
        }
        return firstEffect();
    }

    private ServerGameMessage firstEffect(){
        FullEffect baseEff = selectedWeapon.getBaseEffect();
        FullEffect altEff = null;
        if (selectedWeapon.getAltEffect() != null) {
            if(currPlayer.canUseWeaponEffect(selectedWeapon.getAltEffect())) {
                altEff = selectedWeapon.getAltEffect();
                return new ChooseFirstEffectRequest(baseEff, altEff);
            }
        }
        currFullEffect = baseEff;
        currSimpleEffect = currFullEffect.getSimpleEffect(0);
        baseDone = true;
        nSimpleEffect = 0;
        return currSimpleEffect.handle(this);
        //return managePlusEffectChoice();
    }

    private ServerGameMessage managePlusEffectChoice() {
        List<FullEffect> tmp = new ArrayList<>(remainingPlusEffects);
        if (selectedWeapon.getPlusEffects() != null){
            if(plusBeforeBase != null)
                remainingPlusEffects.remove(plusBeforeBase);
            if (selectedWeapon.isPlusOrder() && currPlayer.canUseWeaponEffect(remainingPlusEffects.get(0))) {
                isOrdered = true; //maybe not necessary
                return new UsePlusByOrderRequest(remainingPlusEffects);
            }
            else {
                for (FullEffect fe : tmp)
                    if (!currPlayer.canUseWeaponEffect(fe))
                        remainingPlusEffects.remove(fe);
                if (!remainingPlusEffects.isEmpty())
                    return new UsePlusEffectRequest(remainingPlusEffects);
                else {

                    return checkShootActionEnd();
                }
            }
        }
        else
            return terminateFullEffect();
    }

    @Override
    public ServerGameMessage handle(ChooseFirstEffectResponse chooseFirstEffectResponse) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

        if(chooseFirstEffectResponse.getN() == 1)
            currFullEffect = selectedWeapon.getBaseEffect();
        else {
            try {
                currFullEffect = selectedWeapon.getAltEffect();
                addFinalPayment(currFullEffect.getPrice(), chooseFirstEffectResponse.getToUse());
            }catch(InsufficientAmmoException e)
            {
                //TODO: ??
                return new InsufficientAmmoResponse();
            }
        }
        currSimpleEffect = currFullEffect.getSimpleEffect(0);
        baseDone = true;
        return currSimpleEffect.handle(this);
    }

    @Override
    public ServerGameMessage handle(UsePlusBeforeResponse usePlusBeforeResponse) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

        if(usePlusBeforeResponse.getT() == 'Y' || usePlusBeforeResponse.getT() == 'y') {
            plusBeforeBase = usePlusBeforeResponse.getPlusEff();
            currFullEffect = plusBeforeBase;
            remainingPlusEffects.remove(plusBeforeBase);
            currSimpleEffect = currFullEffect.getSimpleEffect(0);
            try{
                addFinalPayment(currFullEffect.getPrice(),usePlusBeforeResponse.getToUse());
            }catch(InsufficientAmmoException e)
            {
                //TODO: ??
                return new InsufficientAmmoResponse();
            }
            nSimpleEffect = 0;
            return  currSimpleEffect.handle(this);
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
            if (!toUse.isEmpty()) {
                if (powerUpToPay == null)
                    powerUpToPay = new ArrayList<>(toUse);
                else
                    powerUpToPay.addAll(toUse);
            }
        }
        if(currFullEffect.getPrice() != null)
            if(!currFullEffect.getPrice().isEmpty())
                if(currFullEffect.getPrice().get(0) != Color.ANY)
                    currPlayer.pay(currFullEffect.getPrice(),toUse);
    }

    @Override
    public ServerGameMessage handle(UseOrderPlusResponse useOrderPlusResponse){
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

        if(useOrderPlusResponse.getT() == 'Y' || useOrderPlusResponse.getT() == 'y') {
            /*if(remainingPlusEffects.size()>1)
                remainingPlusEffects = remainingPlusEffects.subList(1,remainingPlusEffects.size());
            else
                remainingPlusEffects = null;*/
            remainingPlusEffects.remove(0);
            currFullEffect = useOrderPlusResponse.getPlusEffects().get(0);
            currSimpleEffect = currFullEffect.getSimpleEffect(0);
            try{
                addFinalPayment(currFullEffect.getPrice(),useOrderPlusResponse.getToUse());
            }catch(InsufficientAmmoException e)
            {
                //TODO: ??
                return new InsufficientAmmoResponse();
            }
            nSimpleEffect = 0;
            return currSimpleEffect.handle(this);
        }
        else{
            remainingPlusEffects.clear();
            return terminateFullEffect();
        }

    }

    private ServerGameMessage terminateFullEffect() {
        if(currFullEffect != null && nSimpleEffect < currFullEffect.getSimpleEffects().size())
            return currFullEffect.getSimpleEffects().get(nSimpleEffect).handle(this);
        if(state != ServerState.WAITING_POWER_USAGE && damageEffect) {
            state = ServerState.WAITING_POWER_USAGE;
            List<CardPower> powers = controlPowerUpDamage();
            if (!powers.isEmpty()) {
                return new AfterDamagePowerUpRequest(powers, selectableTarget);
            }
        }
        else if(state == ServerState.WAITING_POWER_TERMINATE)
            return checkTurnEnd();
        state = ServerState.HANDLING_SHOOT;
        if(ammoToPay != null) {
            if(powerUpToPay != null) {
                clientHandler.sendMessage(new AddPayment(ammoToPay, powerUpToPay));
                powerUpToPay.clear();
            }
            else
                clientHandler.sendMessage(new AddPayment(ammoToPay, new ArrayList<>()));
            ammoToPay.clear();
        }
        return checkShootActionEnd();
    }

    @Override
    public ServerGameMessage handle(UsePlusEffectResponse usePlusEffectResponse) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

        remainingPlusEffects = new ArrayList<>(usePlusEffectResponse.getPlusRemained());
        currFullEffect = usePlusEffectResponse.getEffectToApply();
        currSimpleEffect = currFullEffect.getSimpleEffect(0);
        try {
            addFinalPayment(currFullEffect.getPrice(), usePlusEffectResponse.getToUse());
        }catch(InsufficientAmmoException e){
            //TODO: ??
            return new InsufficientAmmoResponse();
        }
        nSimpleEffect = 0;
        return currSimpleEffect.handle(this);
    }

    @Override
    public ServerGameMessage handle(TerminateShootAction terminateShootAction) {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

        remainingPlusEffects.clear();
        return checkShootActionEnd();
    }


    /**
     * Ask the user for the targets of the next simpleEffect, if there is the need of a choice
     * If the choice is not needed, it passes to the next simpleEffect
     * When simpleEffects are terminated it passes to the next FullEffect
     * When fullEffects are terminated it return a confirm message
     * It's overloaded for MovementEffect
     * @return
     */
    private ServerGameMessage handleOtherEffect(SimpleEffect e)
    {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

        damageEffect = true;
        currSimpleEffect = e;
        if(nSimpleEffect == currFullEffect.getSimpleEffects().size())
            return checkShootActionEnd();
        else
            nSimpleEffect++;

        selectableTarget = e.searchTarget(currPlayer);
        if (selectableTarget.isEmpty()){
            clientHandler.sendMessage(new OperationCompletedResponse("There are no more target to shoot!"));
            return checkShootActionEnd();
        }
        if (selectableTarget.size() > e.getMaxEnemy() || (e.getMinEnemy() < e.getMaxEnemy() && selectableTarget.size() > e.getMinEnemy()))
            return new ChooseTargetRequest(selectableTarget, currSimpleEffect);

        //apply without asking nothing if it is not necessary
        e.applyEffect(currPlayer, selectableTarget);
        return terminateFullEffect();
    }

    private List<CardPower> controlPowerUpDamage() {
        List<CardPower> list = new ArrayList<>();
        for(CardPower cp : currPlayer.getCardPower())
            if(cp.getName().equalsIgnoreCase("Targeting scope"))
                list.add(cp);
        return list;
    }

    /**
     * Ask the user for the targets of the next MovementEffect, if there is the need of a choice
     * If the choice is not needed, it asks for the square where the target has to be moved, if it is needed
     * If no choice is needed it passes to the next SingleEffect
     * When simpleEffects are terminated it passes to the next FullEffect
     * When fullEffects are terminated it return a confirm message
     * @return
     */
    private ServerGameMessage handleMovementEffect(MovementEffect e)
    {
        if(checkIfEnded())
            return new NotifyEndGame(model.getRanking());
        if(model.getCurrentTurn().getCurrentPlayer() != currPlayer)
            return new OperationCompletedResponse("Not your turn!");

        currSimpleEffect = e;
        if(nSimpleEffect == currFullEffect.getSimpleEffects().size())
            return checkShootActionEnd();
        else
            nSimpleEffect++;


        if(!e.isMoveShooter())
        {
            selectableTarget = e.searchTarget(currPlayer);
            if(selectableTarget.isEmpty()) {
                clientHandler.sendMessage(new InvalidWeaponResponse());
                return checkShootActionEnd();
            }
            if(selectableTarget.size() > e.getMaxEnemy() || (e.getMinEnemy() < e.getMaxEnemy() && selectableTarget.size() > e.getMinEnemy()))
                return new ChooseTargetRequest(selectableTarget, currSimpleEffect);
            else
                toBeMoved = (Player)selectableTarget.get(0);
        }
        else{
            toBeMoved = currPlayer;
        }

        selectableSquares = e.selectPosition(toBeMoved);
        if(selectableSquares.isEmpty()){
            clientHandler.sendMessage(new InvalidWeaponResponse());
            return checkShootActionEnd();
        }
        if(selectableSquares.size() > 1)
            return new ChooseSquareRequest(selectableSquares);
        Square start = toBeMoved.getPosition();
        e.applyEffect(toBeMoved,Collections.singletonList(selectableSquares.get(0)));
        if(state == ServerState.WAITING_POWER_USAGE) {
            state = ServerState.WAITING_POWER_TERMINATE;
            return terminateFullEffect();
        }
        List<Player> prevTargets = currPlayer.getActualWeapon().getPreviousTargets();
        prevTargets.remove(toBeMoved);
        prevTargets.add(toBeMoved);
        Square dest = toBeMoved.getPosition();
        currPlayer.getActualWeapon().setLastDirection(GameMap.getDirection(start,dest));
        return terminateFullEffect();
    }


    /**
     * Ask a player to respawn, sending him the drawn powerup card
     */
    @Override
    public void onRespawn() {
        state = ServerState.WAITING_RESPAWN;
        CardPower cp = model.drawPowerUp();
        this.currPlayer.addCardPower(cp);
        clientHandler.sendMessage(new RespawnRequest(cp));
    }

    @Override
    public void onTurnStart() {
        if(state != ServerState.WAITING_SPAWN)
        {
            state = ServerState.WAITING_ACTION;
            clientHandler.sendMessage(new ChooseTurnActionRequest());
        }
        else{
            if(currPlayer.getCardPower().size()<2) {
                CardPower cp = model.drawPowerUp();
                currPlayer.addCardPower(cp);
                clientHandler.sendMessage(new RespawnRequest(cp));
            }
            else
                clientHandler.sendMessage(new RespawnRequest());

        }

    }

    /**
     * When the player is suspended from the game end the current turn if it's his turn and eventually notify him if the suspension is caused by a timeout (and not by a connection error)
     * @param timeOut
     */
    @Override
    public void onSuspend(boolean timeOut) {

        if(model.isEnded())
            this.state = ServerState.GAME_ENDED;
        model.removeGameListener(this.clientHandler);
        if(model.getCurrentTurn().getCurrentPlayer().equals(this.currPlayer) && !model.isEnded())
            endTurnManagement();


    }

    /**
     * Notify the player of its points
     */
    @Override
    public void notifyPoints() {
        clientHandler.sendMessage(new NotifyPoints(this.currPlayer.getPoints()));
    }

    void notifyPlayerExitedFromWaitingRoom(int pId){
        clientHandler.sendMessage(new NotifyPlayerExitedWaitingRoom(pId));
    }

    /**
     * Remove this user from his waiting room
     */
    void leaveWaitingRoom() {
        waitingRoom.removeWaitingPlayer(this);
        if(waitingRoom.isEmpty())
            GameManager.get().removeWaitingRoom(waitingRoom);
    }

    void notifyPlayerJoinedWaitingRoom(Player p)
    {
        clientHandler.sendMessage(new NotifyPlayerJoinedWaitingRoom(p));
    }


    @Override
    public ServerGameMessage handle(MovementEffect e) {
        return handleMovementEffect(e);
    }

    @Override
    public ServerGameMessage handle(PlainDamageEffect e) {
        return handleOtherEffect(e);
    }

    @Override
    public ServerGameMessage handle(SquareDamageEffect e) {
        return handleOtherEffect(e);
    }

    @Override
    public ServerGameMessage handle(RoomDamageEffect e) {
        return handleOtherEffect(e);
    }

    @Override
    public ServerGameMessage handle(AreaDamageEffect e) {
        return handleOtherEffect(e);
    }

    @Override
    public ServerGameMessage handleTarget(MovementEffect movementEffect, List<Target> targetList) {
        return handleTargetSelectionMovement(targetList);
    }

    @Override
    public ServerGameMessage handleTarget(PlainDamageEffect plainDamageEffect, List<Target> targetList) {
        return handleTargetSelection(targetList);
    }

    @Override
    public ServerGameMessage handleTarget(AreaDamageEffect areaDamageEffect, List<Target> targetList) {
        return handleTargetSelection(targetList);
    }

    @Override
    public ServerGameMessage handleTarget(SquareDamageEffect squareDamageEffect, List<Target> targetList) {
        return handleTargetSelection(targetList);
    }

    @Override
    public ServerGameMessage handleTarget(RoomDamageEffect roomDamageEffect, List<Target> targetList) {
        return handleTargetSelection(targetList);
    }


}