package game.controller;

import game.model.*;

import java.util.List;

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

    void grabWeaponNotification(int p, int x, int y);

    void powerUpUsageNotification(int id, String name, String description);

    void choosePowerUpToRespawn(List<CardPower> cardPower);
}
