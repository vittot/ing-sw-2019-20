package game.controller.commands;

import game.controller.commands.servercommands.*;
import game.model.exceptions.InsufficientAmmoException;
import game.model.exceptions.MapOutOfLimitException;

public interface ServerGameMessageHandler {

    void handle (CheckReloadResponse serverMsg);
    void handle (ChooseSingleActionRequest serverMsg);
    void handle (ChooseSquareRequest serverMsg);
    void handle (ChooseTargetRequest serverMsg);
    void handle (ChooseTurnActionRequest serverMsg);
    void handle (InvalidTargetResponse serverMsg);
    void handle (InvalidWeaponResponse serverMsg);
    void handle (InvalidActionResponse serverMsg);
    void handle (InsufficientNumberOfActionResponse serverMsg);
    void handle (InvalidStepResponse serverMsg);
    void handle (MaxNumberOfWeaponsResponse serverMsg);
    void handle (NotifyDamageResponse serverMsg);
    void handle (NotifyDeathResponse serverMsg);
    void handle (NotifyEndGame serverMsg);
    void handle (NotifyGrabWeapon serverMsg);
    void handle (NotifyMovement serverMsg);
    void handle (NotifyPowerUpUsage serverMsg);
    void handle (PickUpAmmoResponse serverMsg);
    void handle (PickUpWeaponResponse serverMsg);
    void handle (RespawnRequest serverMsg);
    void handle (InsufficientAmmoResponse serverMsg);
    void handle (OperationCompletedResponse serverMsg);
    void handle (InvalidPowerUpResponse serverMsg);
    void handle (InvalidGrabPositionResponse serverMsg);
    void handle (AfterDamagePowerUpRequest serverMsg);
    void handle (NotifyGameStarted serverMsg);
    void handle(WaitingRoomsListResponse waitingRoomsListResponse);
    void handle(InvalidMessageResponse invalidMessageResponse);
    void handle(NotifyTurnChanged notifyTurnChanged);
    void handle(NotifyMarks notifyMarks);
    void handle(NotifyGrabCardAmmo notifyGrabCardAmmo);
    void handle(NotifyRespawn notifyRespawn);
    void handle(AvailableMapsListResponse availableMapsListResponse);
    void handle(JoinWaitingRoomResponse joinWaitingRoomResponse);
    void handle(CreateWaitingRoomResponse createWaitingRoomResponse);
    void handle(ChooseWeaponToGrabRequest chooseWeaponToGrabRequest);
    void handle(ReloadWeaponAsk reloadWeaponAsk);
    void handle(NotifyPlayerSuspend notifyPlayerSuspend);
    void handle(TimeOutNotify timeOutNotify);
    void handle(UserAlreadyLoggedResponse userAlreadyLoggedResponse);
    void handle(UserLoggedResponse userLoggedResponse);
    void handle(RejoinGameRequest rejoinGameRequest);
    void handle(NotifyPlayerRejoin notifyPlayerRejoin);
    void handle(RejoinGameConfirm rejoinGameConfirm);
    void handle(ChooseWeaponToShootRequest chooseWeaponToShootRequest);
    void handle(ChooseFirstEffectRequest chooseFirstEffectRequest);
    void handle(BeforeBaseRequest beforeBaseRequest);
    void handle(UsePlusEffectRequest usePlusEffectRequest);
    void handle(UsePlusByOrderRequest usePlusByOrderRequest);
    void handle(ShootActionResponse shootActionResponse);
    void handle(NotifyPlayerExitedWaitingRoom notifyPlayerExitedWaitingRoom);
    void handle(NotifyPlayerJoinedWaitingRoom notifyPlayerJoinedWaitingRoom);
    void handle(RemoveSpawnPowerUp removeSpawnPowerUp);
    void handle(NotifyWeaponRefill notifyWeaponRefill);
    void handle(NotifyAmmoRefill notifyAmmoRefill);
    void handle(UpdateMarks updateMarks);
    void handle(ChoosePowerUpUsed choosePowerUpUsed);
    void handle(AddPayment addPayment);
    void handle(NotifyPoints notifyPoints);
}
