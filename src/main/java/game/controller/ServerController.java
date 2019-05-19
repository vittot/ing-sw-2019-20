package game.controller;

import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
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

public class ServerController implements ClientMessageHandler, RespawnObserver {
    // reference to the Networking layer
    private SocketClientHandler clientHandler;

    // reference to the Model
    private Game model;
    private Player currPlayer;
    private CardWeapon selectedWeapon;
    private List<FullEffect> selectedPlusEffects;
    private FullEffect baseAltEffect;
    private FullEffect currFullEffect;
    private SimpleEffect currSimpleEffect;
    private Player toBeMoved;
    private boolean baseDone;
    private int nSimpleEffect; //the number of the current simpleEffect of the current FullEffect
    private List<Target> selectableTarget;
    private List<Square> selectableSquares;
    private ServerState state;


    public ServerController(SocketClientHandler clientHandler) {

        this.clientHandler = clientHandler;
    }

    public ServerController(){ }

    public SocketClientHandler getClientHandler() {
        return clientHandler;
    }

    /**
     * Called by the WaitingRoom when it's ready to start the game, it notify the client associated to this server controller that the game is started, sending the Game serialized object
     *
     * @param g
     */
    void startGame(Game g, Player p)
    {
        state = ServerState.WAITING_SPAWN; //TODO management of the first spawn of the players in order
        this.model = g;
        this.currPlayer = p;
        CardPower c1 = g.drawPowerUp();
        CardPower c2 = g.drawPowerUp();
        this.currPlayer.addCardPower(c1);
        this.currPlayer.addCardPower(c2);
        this.currPlayer.addAmmo(Color.BLUE);
        this.currPlayer.addAmmo(Color.YELLOW);
        this.currPlayer.addAmmo(Color.RED);
        this.model.addNewPlayer(this.currPlayer);
        this.clientHandler.sendMessage(new NotifyGameStarted(this.currPlayer,g.getMap(),c1,c2));
    }

    @Override
    public ServerMessage handle(ChooseSquareResponse clientMsg) {
        Square selectedSquare = clientMsg.getSelectedSquare();
        if(!selectableSquares.contains(selectedSquare))
            return new InvalidTargetResponse();

        currSimpleEffect.applyEffect(toBeMoved,Collections.singletonList(selectedSquare));
        if(currFullEffect != null)
            return handleEffect(currFullEffect.getSimpleEffects().get(nSimpleEffect));

        return new OperationCompletedResponse();

    }

    /**
     * Validate the selected target for an effect and apply it, or ask for the movement position if it's a MovementEffect
     * @param clientMsg
     * @return
     */
    @Override
    public ServerMessage handle(ChooseTargetResponse clientMsg)
    {
        List<Target> selectedTarget = clientMsg.getSelectedTargets();
        if(!selectableTarget.containsAll(selectedTarget))
            return new InvalidTargetResponse();

        if(!validateSelectedTargets(selectedTarget,currSimpleEffect))
            return new InvalidTargetResponse();

        //in case no target has been selected (and it's allowed) it stops
        if(selectedTarget.isEmpty())
            return new OperationCompletedResponse();

        return handleTargetSelection(selectedTarget,currSimpleEffect);
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
        if(currFullEffect != null)
            return handleEffect(currFullEffect.getSimpleEffects().get(nSimpleEffect));
        return new OperationCompletedResponse();
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
            if (Action.checkAction(clientMsg.getTypeOfAction())) {
                currPlayer.getGame().getCurrentTurn().newAction(clientMsg.getTypeOfAction(), currPlayer.getAdrenaline());
                return new OperationCompletedResponse("Action completed"); //TODO check this
            } else {
                return new InvalidActionResponse();
            }
        }
        catch(NoResidualActionAvaiableException e){
            return new InsufficientNumberOfActionResponse();
        }
    }

    //TODO probably useless
    @Override
    public ServerMessage handle(GrabActionRequest clientMsg) {
        return null;
    }

    @Override
    public ServerMessage handle(MovementActionRequest clientMsg) {
        if(currPlayer.getGame().getCurrentTurn().applyStep(Action.MOVEMENT)){
            return new ChooseSquareRequest(currPlayer.getPosition().getSquaresInDirections(1,1));
        }
        else{
            return new InvalidStepResponse();
        }
    }

    @Override
    public ServerMessage handle(PickUpAmmoRequest clientMsg) {
        if(!currPlayer.getGame().getCurrentTurn().applyStep(Action.GRAB))
            return new InvalidStepResponse();
        try{
            currPlayer.pickUpAmmo();
            return new OperationCompletedResponse("Ammo picked up"); //TODO check this
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
        if(!currPlayer.getGame().getCurrentTurn().applyStep(Action.GRAB))
            return new InvalidStepResponse();
        if(!currPlayer.getPosition().getWeapons().contains(clientMsg.getWeapon()))
            return new InvalidWeaponResponse();
        if(!currPlayer.getCardPower().containsAll(clientMsg.getPowerup()))
            return new InvalidPowerUpResponse();
        try {
            currPlayer.pickUpWeapon(clientMsg.getWeapon(), clientMsg.getWeaponToWaste(), clientMsg.getPowerup());
            return  new OperationCompletedResponse("Weapon picked up"); //TODO check this
        }catch (InsufficientAmmoException e){
            return new InsufficientAmmoResponse();
        }
        catch (NoCardWeaponSpaceException x){
            return new MaxNumberOfWeaponsResponse();
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
        if(!currPlayer.getCardPower().containsAll(clientMsg.getPowerups()))
            return new InvalidPowerUpResponse();
        if( w == null)
            return new InvalidWeaponResponse();
        if( w.isLoaded())
            return new InvalidWeaponResponse();
        try{
            w.reloadWeapon(clientMsg.getPowerups());
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
                if (model.getCurrentTurn().getCurrentPlayer().equals(currPlayer)) {
                    state = ServerState.WAITING_ACTION;
                    return new ChooseTurnActionRequest();
                } else {
                    state = ServerState.WAITING_TURN;
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
        CardWeapon w = currPlayer.getWeapons().stream().filter(wp -> wp.getId() == clientMsg.getWeapon().getId()).findFirst().orElse(null);
        if( w == null)
            return new InvalidWeaponResponse();
        if( !w.isLoaded())
            return new InvalidWeaponResponse();
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
        return handleEffect(currFullEffect.getSimpleEffects().get(nSimpleEffect));
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
            return new JoinWaitingRoomResponse(n);
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
        List<Player> toBeRespawned = model.changeTurn();
        for(Player p: toBeRespawned)
        {
            p.notifyRespawnListener();
        }
        return new OperationCompletedResponse("Your turn is terminated");
    }

    @Override
    public ServerMessage handle(GetAvailableMapsRequest getAvailableMapsRequest) {
        return new AvailableMapsListResponse(Game.getAvailableMaps());
    }

    @Override
    public ServerMessage handle(ChoosePowerUpResponse choosePowerUpResponse) {
        //TODO later
        return null;
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
        if(nSimpleEffect == currFullEffect.getSimpleEffects().size() - 1)
        {
            nSimpleEffect=0;
            setNextFullEffect();
        }
        else
            nSimpleEffect++;
        selectableTarget = e.searchTarget(currPlayer);
        if(selectableTarget.isEmpty())
            return new InvalidWeaponResponse();

        if(selectableTarget.size() > e.getMaxEnemy() || (e.getMinEnemy() < e.getMaxEnemy() && selectableTarget.size() > e.getMinEnemy()))
            return new ChooseTargetRequest(selectableTarget);

        //apply without asking nothing if it is not necessary
        e.applyEffect(currPlayer,selectableTarget);
        if(currFullEffect != null)
            return handleEffect(currFullEffect.getSimpleEffects().get(nSimpleEffect));

        return new OperationCompletedResponse();

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
        if(nSimpleEffect == currFullEffect.getSimpleEffects().size() - 1)
        {
            nSimpleEffect=0;
            setNextFullEffect();
        }
        else
            nSimpleEffect++;

        if(!e.isMoveShooter())
        {
            selectableTarget = e.searchTarget(currPlayer);
            if(selectableTarget.isEmpty())
                return new InvalidWeaponResponse();
            if(selectableTarget.size() > e.getMaxEnemy() || (e.getMinEnemy() < e.getMaxEnemy() && selectableTarget.size() > e.getMinEnemy()))
                return new ChooseTargetRequest(selectableTarget);
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


    /**
     * Sets the next FullEffect in the list of requested effects, when they are finished it becomes null
     */
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
    }


    /**
     * Ask a player to respawn, sending him the drawn powerup card
     */
    @Override
    public void onRespawn() {
        //TODO state in waiting for client response
        clientHandler.sendMessage(new RespawnRequest(model.drawPowerUp()));
    }
}
