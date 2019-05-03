package game.controller.commands;

import game.controller.commands.clientcommands.*;

public interface ClientMessageHandler {

    ServerMessage handle (ChooseSquareResponse clientMsg);
    ServerMessage handle (ChooseTargetResponse clientMsg);
    ServerMessage handle (ChooseTurnActionResponse clientMsg);
    ServerMessage handle (GrabActionRequest clientMsg);
    ServerMessage handle (MovementActionRequest clientMsg);
    ServerMessage handle (PickUpAmmoRequest clientMsg);
    ServerMessage handle (PickUpWeaponRequest clientMsg);
    ServerMessage handle (ReloadWeaponRequest clientMsg);
    ServerMessage handle (RespawnResponse clientMsg);
    ServerMessage handle (ShootActionRequest clientMsg);







}
