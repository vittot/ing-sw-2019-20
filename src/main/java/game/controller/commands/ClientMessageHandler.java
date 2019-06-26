package game.controller.commands;

import game.controller.commands.clientcommands.*;
import game.model.exceptions.InsufficientAmmoException;
import game.model.exceptions.MapOutOfLimitException;
import game.model.exceptions.NoCardAmmoAvailableException;

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

    ServerMessage handle(GetWaitingRoomsRequest getWaitingRoomsRequest);

    ServerMessage handle(JoinWaitingRoomRequest joinWaitingRoomRequest);

    ServerMessage handle(CreateWaitingRoomRequest createWaitingRoomRequest);

    ServerMessage handle(EndTurnRequest endTurnRequest);

    ServerMessage handle(GetAvailableMapsRequest getAvailableMapsRequest);

    ServerMessage handle(ChoosePowerUpResponse choosePowerUpResponse);

    ServerMessage handle(LoginMessage loginMessage);

    ServerMessage handle(RejoinGameResponse rejoinGameResponse);

    ServerMessage handle(ChooseWeaponToShootResponse chooseWeaponToShootResponse);

    ServerMessage handle(ChooseFirstEffectResponse chooseFirstEffectResponse);

    ServerMessage handle(UsePlusBeforeResponse usePlusBeforeResponse);

    ServerMessage handle(UseOrderPlusResponse useOrderPlusResponse);

    ServerMessage handle(UsePlusEffectResponse usePlusEffectResponse);

    ServerMessage handle(TerminateShootAction terminateShootAction);

    ServerMessage handle(CheckValidWeaponRequst clientMsg);

    ServerMessage handle(ChooseSquareToShootResponse chooseSquareToShootResponse);

    ServerMessage handle(CounterAttackResponse counterAttackResponse);
}
