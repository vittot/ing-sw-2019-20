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

public class ServerController implements ClientMessageHandler {
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
    private boolean baseDone;
    private int nSimpleEffect; //the number of the current simpleEffect of the current FullEffect
    private List<Target> selectableTarget;


    public ServerController(SocketClientHandler clientHandler) {
        this.clientHandler = clientHandler;
    }

    /**
     * Create a new Game for this client
     */
    public void setNewGame()
    {

    }

    /**
     * Join the client to an existing Game
     * @param gameId
     */
    public void joinGame(int gameId){

    }

    // TODO: ------ ClientMessage handling

    @Override
    public ServerMessage handle(ChooseSquareResponse clientMsg) {
        return null;
    }

    @Override
    public ServerMessage handle(ChooseTargetResponse clientMsg)
    {
        List<Target> selectedTarget = clientMsg.selectedTargets;
        if(selectedTarget.size() > currFullEffect.getSimpleEffect(nSimpleEffect).getMaxEnemy())
            return new InvalidTargetResponse();
        if(!selectableTarget.containsAll(selectedTarget))
            return new InvalidTargetResponse();

        currSimpleEffect.applyEffect(currPlayer,selectedTarget);
        if(currFullEffect != null)
            return handleEffect();

        return new OperationCompletedResponse();

    }

    private ServerMessage handleTargetSelection(List<Target> selectedTarget, SimpleEffect s)
    {
        s.applyEffect(currPlayer,selectedTarget);
        return new OperationCompletedResponse();
    }


    private ServerMessage handleTargetSelection(List<Target> selectedTarget, MovementEffect s)
    {
        s.applyEffect(currPlayer,selectedTarget);
        List<Square> possiblePosition = s.selectPosition(currPlayer);
        return new ChooseSquareRequest(possiblePosition);
    }

    @Override
    public ServerMessage handle(ChooseTurnActionResponse clientMsg) {
        try {
            if (Action.checkAction(clientMsg.typeOfAction)) {
                currPlayer.getGame().getCurrentTurn().newAction(clientMsg.typeOfAction, currPlayer.getAdrenaline());
                return new OperationCompletedResponse(clientMsg);
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
            return new OperationCompletedResponse(clientMsg);
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
            return  new OperationCompletedResponse(clientMsg);
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
            return new OperationCompletedResponse(clientMsg);
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
                return new OperationCompletedResponse(clientMsg);
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
            currFullEffect = selectedPlusEffects.stream().filter(e -> e.isBeforeBase()).findFirst().orElse(null);
            selectedPlusEffects.remove(currFullEffect);
        }
        else
        {
            baseDone = true;
            currFullEffect = baseAltEffect;

        }
        return handleEffect();


    }

    /**
     * Ask the user for the targets of the next simpleEffect in the currentEffect, if there is the need of a choice
     * If the choice is not needed, it passes to the next simpleEffect
     * When simpleEffects are terminated it passes to the next FullEffect
     * When fullEffects are terminated it return a confirm message
     * @return
     */
    private ServerMessage handleEffect()
    {
        currSimpleEffect = currFullEffect.getSimpleEffects().get(nSimpleEffect);
        if(nSimpleEffect == currFullEffect.getSimpleEffects().size() - 1)
        {
            nSimpleEffect=0;
            setNextFullEffect();
        }
        else
            nSimpleEffect++;
        selectableTarget = currSimpleEffect.searchTarget(currPlayer);
        if(selectableTarget.size() > currSimpleEffect.getMaxEnemy() || (currSimpleEffect.getMinEnemy() < currSimpleEffect.getMaxEnemy() && selectableTarget.size() > currSimpleEffect.getMinEnemy()))
            return new ChooseTargetRequest(selectableTarget);

        //apply without ask
        currSimpleEffect.applyEffect(currPlayer,selectableTarget);
        if(currFullEffect != null)
            return handleEffect();

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



}
