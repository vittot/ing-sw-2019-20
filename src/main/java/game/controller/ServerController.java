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
    private final SocketClientHandler clientHandler;

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


    public ServerController(SocketClientHandler clientHandler) {

        this.clientHandler = clientHandler;
    }

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
        this.model = g;
        this.currPlayer = p;
        this.clientHandler.sendMessage(new NotifyGameStarted(g));
        if(g.getCurrentTurn().getCurrentPlayer().equals(p))
            clientHandler.sendMessage(new ChooseTurnActionRequest());
    }

    // TODO: ------ ClientMessage handling

    @Override
    public ServerMessage handle(ChooseSquareResponse clientMsg) {
        Square selectedSquare = clientMsg.selectedSquare;
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
        List<Target> selectedTarget = clientMsg.selectedTargets;
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
            if (Action.checkAction(clientMsg.typeOfAction)) {
                currPlayer.getGame().getCurrentTurn().newAction(clientMsg.typeOfAction, currPlayer.getAdrenaline());
                return new OperationCompletedResponse("Action completed"); //TODO check this
            } else {
                return new InvalidActionResponse();
            }
        }
        catch(NoResidualActionAvaiableException e){
            return new InvalidNumberOfActionResponse();
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
            return new InvalidGrabPositionRsponse();
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
        if(!currPlayer.getPosition().getWeapons().contains(clientMsg.weapon))
            return new InvalidWeaponResponse();
        if(!currPlayer.getCardPower().containsAll(clientMsg.powerup))
            return new InvalidPowerUpResponse();
        try {
            currPlayer.pickUpWeapon(clientMsg.weapon, clientMsg.weaponToWaste, clientMsg.powerup);
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
        CardWeapon w = currPlayer.getWeapons().stream().filter(wp -> wp.getId() == clientMsg.weapon.getId()).findFirst().orElse(null);
        if(!currPlayer.getCardPower().containsAll(clientMsg.powerups))
            return new InvalidPowerUpResponse();
        if( w == null)
            return new InvalidWeaponResponse();
        if( w.isLoaded())
            return new InvalidWeaponResponse();
        try{
            w.reloadWeapon(clientMsg.powerups);
            return new CheckReloadResponse(clientMsg.weapon, clientMsg.powerups);
        }catch(InsufficientAmmoException e)
        {
            return new InsufficientAmmoResponse();
        }
    }

    @Override
    public ServerMessage handle(RespawnResponse clientMsg) {
        if(currPlayer.isDead()) {
            if (currPlayer.getCardPower().contains(clientMsg.powerUp)) {
                currPlayer.respawn(clientMsg.powerUp);
                currPlayer.removePowerUp(Collections.singletonList(clientMsg.powerUp));
                return new OperationCompletedResponse("You are respawned!"); //TODO check this
            }
            else
                return new InvalidPowerUpResponse();
        }
        else
            return new InvalidDeathResponse();
    }

    @Override
    public ServerMessage handle(ShootActionRequest clientMsg) {
        CardWeapon w = currPlayer.getWeapons().stream().filter(wp -> wp.getId() == clientMsg.weapon.getId()).findFirst().orElse(null);
        if( w == null)
            return new InvalidWeaponResponse();
        if( !w.isLoaded())
            return new InvalidWeaponResponse();
        if( (clientMsg.baseEffect != null && clientMsg.altEffect != null))
            return new InvalidWeaponResponse();
        if(clientMsg.baseEffect != null && !clientMsg.baseEffect.equals(w.getBaseEffect()))
            return new InvalidWeaponResponse();
        if(clientMsg.altEffect != null && !clientMsg.altEffect.equals((w.getAltEffect())))
            return new InvalidWeaponResponse();
        if(!w.getPlusEffects().containsAll(clientMsg.plusEffects))
            return new InvalidWeaponResponse();
        if(!w.isPlusBeforeBase() && clientMsg.plusBeforeBase)
            return new InvalidWeaponResponse();
        if(w.isPlusOrder())
        {
            for(int i=0;i<clientMsg.plusEffects.size();i++)
                if(!clientMsg.plusEffects.get(i).equals(w.getPlusEffects().get(i)))
                    return new InvalidWeaponResponse();
        }

        List<Color> totalAmmo = new ArrayList<>();
        for(FullEffect fe : clientMsg.plusEffects)
            totalAmmo.addAll(fe.getPrice());
        if(clientMsg.altEffect != null)
            totalAmmo.addAll(clientMsg.altEffect.getPrice());

        try{
            currPlayer.pay(totalAmmo,clientMsg.paymentWithPowerUp);
        }catch (InsufficientAmmoException ex)
        {
            return new InvalidWeaponResponse();
        }

        selectedWeapon = w;
        selectedPlusEffects = clientMsg.plusEffects;
        nSimpleEffect = 0;
        baseAltEffect = (clientMsg.baseEffect != null) ? clientMsg.baseEffect : clientMsg.altEffect;
        if(clientMsg.plusBeforeBase)
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
        if(w != null)
            return new OperationCompletedResponse();
        return new InvalidMessageResponse("The indicated room id does not exist on the Server");
    }

    @Override
    public ServerMessage handle(CreateWaitingRoomRequest createWaitingRoomRequest) {
        GameManager.get().addWaitingRoom(createWaitingRoomRequest.getMapId(),createWaitingRoomRequest.getNumWaitingPlayers());
        return new OperationCompletedResponse("Waiting room correctly created");
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
