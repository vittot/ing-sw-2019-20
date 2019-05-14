package game.controller;

import game.model.*;

import java.util.List;
import java.util.Map;

public interface View {

    void setUserNamePhase();

    void insufficientAmmoNotification();

    void chooseStepActionPhase(List<Action> possibleAction);

    void chooseSquarePhase(List<Square> possiblePositions);

    void chooseTargetPhase(List<Target> possibleTargets);

    void chooseTurnActionPhase();

    void invalidTargetNotification();

    void invalidWeaponNotification();

    void invalidActionNotification();

    void insufficientNumberOfActionNotification();

    void invalidStepNotification();

    void maxNumberOfWeaponNotification();

    void damageNotification(int shooterId, int damage, int hit);

    void notifyDeath(Kill kill);

    void grabWeaponNotification(int pID, String name, int x, int y);

    void powerUpUsageNotification(int id, String name, String description);

    void choosePowerUpToRespawn(List<CardPower> cardPower);

    void showRanking(Map<Player, Integer> ranking);

    void notifyCompletedOperation(String message);

    void notifyInvalidPowerUP();

    void notifyInvalidGrabPosition();

    void choosePowerUpToUse(List<CardPower> cardPower);

    void notifyStart();

    void notifyInvalidMessage();

    void notifyTurnChanged(int pID);

    void notifyMarks(int marks, int idHitten, int idShooter);

    void notifyGrabAmmo(int pID);

    void notifyRespawn(int pID);

    void chooseRoomPhase(List<WaitingRoom> waitingRooms);

    void showMapsPhase(List<GameMap> availableMaps);
}
