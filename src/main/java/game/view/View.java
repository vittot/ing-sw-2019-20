package game.view;

import game.controller.ClientController;
import game.controller.WaitingRoom;
import game.model.*;
import game.model.effects.FullEffect;
import game.model.effects.SimpleEffect;

import java.util.List;
import java.util.SortedMap;

public interface View {

    void notifyStart();

    void setUserNamePhase();

    void insufficientAmmoNotification();

    void chooseStepActionPhase();

    void chooseSquarePhase(List<Square> possiblePositions);

    void chooseTargetPhase(List<Target> possibleTargets);

    void chooseTurnActionPhase(boolean isMovementAllowed);

    void invalidTargetNotification();

    void invalidWeaponNotification();

    void invalidActionNotification();

    void insufficientNumberOfActionNotification();

    void invalidStepNotification();

    void maxNumberOfWeaponNotification();

    void damageNotification(int shooterId, int damage, int hit);

    void powerUpUsageNotification(String nick, String name, String desc);

    void notifyMovement(int pId, int newX, int newY);

    void grabWeaponNotification(int pID, String name, int x, int y);

    void choosePowerUpToRespawn(List<CardPower> cardPower);

    void showRanking(SortedMap<Player,Integer> ranking);

    void notifyCompletedOperation(String message);

    void notifyInvalidPowerUP();

    void notifyInvalidGrabPosition();

    void choosePowerUpToUse(List<CardPower> cardPower);

    void notifyGrabAmmo(List<Color> ammos, List <CardPower> cp);

    void notifyInvalidMessage();

    void notifyTurnChanged(int pID);

    void notifyMarks(int marks, int idHitten, int idShooter);

    void notifyGrabCardAmmo(int pID);

    void notifyRespawn(int pID);

    void chooseWeaponToGrab(List<CardWeapon> weapons);

    void rejoinGamePhase(List<String> otherPlayers);

    void chooseRoomPhase(List<WaitingRoom> waitingRooms);

    void showMapsPhase(List<GameMap> availableMaps);

    void reloadWeaponPhase(List<CardWeapon> weaponsToReload);

    void showReloadMessage(CardWeapon cW);

    void chooseWeaponToShoot(List<CardWeapon> myWeapons);

    void chooseFirstEffect(FullEffect baseEff, FullEffect altEff);

    void usePlusBeforeBase(FullEffect plusEff);

    void usePlusInOrder(List<FullEffect> plusEffects);

    void choosePlusEffect(List<FullEffect> plusEffects);

    void notifyPlayerSuspended(Player p);

    void timeOutPhase();

    void alreadyLoggedPhase();

    void loginCompletedPhase();

    void rejoinGameConfirm();

    void notifyPlayerRejoin(Player p);

    void notifyPlayerLeavedWaitingRoom(Player p);

    void notifyPlayerJoinedWaitingRoom(Player p);

    void setController(ClientController clientController);

    void waitStart();

    void chooseConnection();

    void notifyConnectionError();

    void connectionFailed();

    void chooseCounterAttack(List<CardPower> counterattack, Player shooter);

    void notifyReconnected();

    void showPoints();

    void notifyDeath(int idKiller, int idVictim, boolean rage);

    void notifyRage(Player killer, Player victim);

    void showNoWeaponToReload();

    void notifyFinalFrenzy();

    void completedShootAction();

}