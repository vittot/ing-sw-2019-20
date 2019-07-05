package game.controller.commands;

import game.controller.commands.clientcommands.*;

/**
 * Interface which is implemented by the controller which is able to handle all client game messages
 */
public interface ClientGameMessageHandler {
    ServerGameMessage handle (ChooseSquareResponse clientMsg);

    ServerGameMessage handle (ChooseTargetResponse clientMsg);

    ServerGameMessage handle (ChooseTurnActionResponse clientMsg);

    ServerGameMessage handle (GrabActionRequest clientMsg);

    ServerGameMessage handle (MovementActionRequest clientMsg);

    ServerGameMessage handle (PickUpAmmoRequest clientMsg);

    ServerGameMessage handle (PickUpWeaponRequest clientMsg);

    ServerGameMessage handle (ReloadWeaponRequest clientMsg);

    ServerGameMessage handle (RespawnResponse clientMsg);

    ServerGameMessage handle (ShootActionRequest clientMsg);

    ServerGameMessage handle(GetWaitingRoomsRequest getWaitingRoomsRequest);

    ServerGameMessage handle(JoinWaitingRoomRequest joinWaitingRoomRequest);

    ServerGameMessage handle(CreateWaitingRoomRequest createWaitingRoomRequest);

    ServerGameMessage handle(EndActionRequest endActionRequest);

    ServerGameMessage handle(GetAvailableMapsRequest getAvailableMapsRequest);

    ServerGameMessage handle(ChoosePowerUpResponse choosePowerUpResponse);

    ServerGameMessage handle(LoginMessage loginMessage);

    ServerGameMessage handle(RejoinGameResponse rejoinGameResponse);

    ServerGameMessage handle(ChooseWeaponToShootResponse chooseWeaponToShootResponse);

    ServerGameMessage handle(ChooseFirstEffectResponse chooseFirstEffectResponse);

    ServerGameMessage handle(UsePlusBeforeResponse usePlusBeforeResponse);

    ServerGameMessage handle(UseOrderPlusResponse useOrderPlusResponse);

    ServerGameMessage handle(UsePlusEffectResponse usePlusEffectResponse);

    ServerGameMessage handle(TerminateShootAction terminateShootAction);

    ServerGameMessage handle(CheckValidWeaponRequest clientMsg);

    ServerGameMessage handle(ChooseSquareToShootResponse chooseSquareToShootResponse);

    ServerGameMessage handle(CounterAttackResponse counterAttackResponse);

    ServerGameMessage handle(LogoutRequest logoutRequest);

    ServerGameMessage handle(EndTurnRequest endTurnRequest);

    ServerGameMessage handle(ReloadWeaponAction reloadWeaponAction);
}
