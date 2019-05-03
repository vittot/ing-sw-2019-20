package game.controller;

import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.controller.commands.clientcommands.*;
import game.controller.commands.servercommands.*;
import game.model.CardWeapon;
import game.model.Game;
import game.model.Player;
import game.model.exceptions.InsufficientAmmoException;

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
        return null;
    }

    @Override
    public ServerMessage handle(GrabActionRequest clientMsg) {
        return null;
    }

    @Override
    public ServerMessage handle(MovementActionRequest clientMsg) {
        return null;
    }

    @Override
    public ServerMessage handle(PickUpAmmoRequest clientMsg) {
        return null;
    }

    @Override
    public ServerMessage handle(PickUpWeaponRequest clientMsg) {
        return null;
    }

    /**
     * Reload a weapon, if possible
     * @param clientMsg
     * @return InvalidWeaponResponse if the Player does not own this weapon or if it is already loaded
     *         InsufficientAmmoResponse if the Player does not have enough ammo/power up cards to pay
     *         OperationCompletedResponse if the weapon has been reloaded correctly
     */
    @Override
    public ServerMessage handle(ReloadWeaponRequest clientMsg) {
        CardWeapon w = currPlayer.getWeapons().stream().filter(wp -> wp.getId() == clientMsg.weapon.getId()).findFirst().orElse(null);
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
        return null;
    }

    @Override
    public ServerMessage handle(ShootActionRequest clientMsg) {
        return null;
    }


}
