package game.controller.commands;

import game.controller.commands.servercommands.*;

public interface ServerMessageHandler {

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
    void handle (NotifyEndGameResponse serverMsg);
    void handle (NotifyGrabWeapon serverMsg);
    void handle (NotifyMovement serverMsg);
    void handle (NotifyPowerUpUsage serverMsg);
    void handle (PickUpAmmoResponse serverMsg);
    void handle (PickUpWeaponResponse serverMsg);
    void handle (RespawnRequest serverMsg);
    void handle (InsufficientAmmoResponse serverMsg);
    void handle (OperationCompletedResponse serverMsg);
    void handle (InvalidPowerUpResponse serverMsg);
    void handle (InvalidGrabPositionRsponse serverMsg);
    void handle (AfterDamagePowerUpRequest serverMsg);
    void handle (NotifyGameStarted serverMsg);
    void handle(WaitingRoomsListResponse waitingRoomsListResponse);
    void handle(InvalidMessageResponse invalidMessageResponse);
    void handle(NotifyTurnChanged notifyTurnChanged);
    void handle(NotifyMarks notifyMarks);
    void handle(NotifyGrabAmmo notifyGrabAmmo);
    void handle(NotifyRespawn notifyRespawn);
}
