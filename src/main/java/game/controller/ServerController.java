package game.controller;

import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.controller.commands.clientcommands.*;
import game.controller.commands.servercommands.*;
import game.model.*;
import game.model.exceptions.InsufficientAmmoException;
import game.model.exceptions.NoResidualActionAvaiableException;

import java.util.ArrayList;
import java.util.Collections;
import game.model.exceptions.NoCardAmmoAvailable;

public class ServerController implements ClientMessageHandler {
    // reference to the Networking layer
    private final SocketClientHandler clientHandler;

    // reference to the Model
    private Game model;
    private Player currPlayer;

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
    public ServerMessage handle(ChooseTargetResponse clientMsg) {
        return null;
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

    @Override
    public ServerMessage handle(GrabActionRequest clientMsg) {
        return null;
    }

    @Override
    public ServerMessage handle(MovementActionRequest clientMsg) {
        if(currPlayer.getGame().getCurrentTurn().applyStep(Action.MOVEMENT)){
            return new ChooseSquareRequest(); // has to be generated a list of possible square?
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
        }catch (NoCardAmmoAvailable e){
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
        if(!currPlayer.getCardPower().contains(clientMsg.powerup))
            return new InvalidPowerUpResponse();
        try {
            currPlayer.pickUpWeapon(clientMsg.weapon, clientMsg.weaponToWaste, clientMsg.powerup);
            return  new OperationCompletedResponse(clientMsg);
        }catch (InsufficientAmmoException e){
            return new InsufficientAmmoResponse();
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
        if(!currPlayer.getCardPower().contains(clientMsg.powerups))
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
        return null;
    }


}
