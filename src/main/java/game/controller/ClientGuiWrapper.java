package game.controller;

import game.model.*;
import game.model.effects.FullEffect;
import game.model.effects.SimpleEffect;

import java.util.List;
import java.util.SortedMap;

import static com.sun.javafx.application.PlatformImpl.runLater;

public class ClientGuiWrapper implements View {

    private ClientGUIView gui;

    public ClientGuiWrapper() {
        this.gui = ClientGUIView.getInstance();
    }

    @Override
    public void notifyStart() {
        runLater(gui::notifyStart);
    }

    @Override
    public void setUserNamePhase() {
        runLater(gui::setUserNamePhase);
    }

    @Override
    public void insufficientAmmoNotification() {
        runLater(gui::insufficientAmmoNotification);
    }

    @Override
    public void chooseStepActionPhase() {
        runLater(gui::chooseStepActionPhase);
    }

    @Override
    public void chooseSquarePhase(List<Square> possiblePositions) {
        runLater(()->gui.chooseSquarePhase(possiblePositions));
    }

    @Override
    public void chooseTargetPhase(List<Target> possibleTargets, SimpleEffect currSimpleEffect) {
        runLater(()->gui.chooseTargetPhase(possibleTargets,currSimpleEffect));
    }

    @Override
    public void chooseTurnActionPhase() {
        runLater(gui::chooseTurnActionPhase);
    }

    @Override
    public void invalidTargetNotification() {
        runLater(gui::invalidTargetNotification);
    }

    @Override
    public void invalidWeaponNotification() {
        runLater(gui::invalidWeaponNotification);
    }

    @Override
    public void invalidActionNotification() {
        runLater(gui::invalidActionNotification);
    }

    @Override
    public void insufficientNumberOfActionNotification() {
        runLater(gui::insufficientNumberOfActionNotification);
    }

    @Override
    public void invalidStepNotification() {
        runLater(gui::invalidStepNotification);
    }

    @Override
    public void maxNumberOfWeaponNotification() {
        runLater(gui::maxNumberOfWeaponNotification);
    }

    @Override
    public void damageNotification(int shooterId, int damage, int hit) {
        runLater(()->gui.damageNotification(shooterId,damage,hit));
    }

    @Override
    public void notifyMovement(int pId, int newX, int newY) {
        runLater(()->gui.notifyMovement(pId,newX,newY));
    }

    @Override
    public void notifyDeath(Kill kill) {
        runLater(()->notifyDeath(kill));
    }

    @Override
    public void grabWeaponNotification(int pID, String name, int x, int y) {
        runLater(()->grabWeaponNotification(pID,name,x,y));
    }

    @Override
    public void powerUpUsageNotification(int id, String name, String description) {
        runLater(()->gui.powerUpUsageNotification(id,name,description));
    }

    @Override
    public void choosePowerUpToRespawn(List<CardPower> cardPower) {
        runLater(()->gui.choosePowerUpToRespawn(cardPower));
    }

    @Override
    public void showRanking(SortedMap<Player, Integer> ranking) {
        runLater(()->showRanking(ranking));
    }

    @Override
    public void notifyCompletedOperation(String message) {
        runLater(()->gui.notifyCompletedOperation(message));
    }

    @Override
    public void notifyInvalidPowerUP() {
        runLater(gui::notifyInvalidPowerUP);
    }

    @Override
    public void notifyInvalidGrabPosition() {
        runLater(gui::notifyInvalidGrabPosition);
    }

    @Override
    public void choosePowerUpToUse(List<CardPower> cardPower) {
        runLater(()->gui.choosePowerUpToUse(cardPower));
    }

    @Override
    public void notifyInvalidMessage() {
        runLater(gui::notifyInvalidMessage);
    }

    @Override
    public void notifyTurnChanged(int pID) {
        runLater(()->notifyTurnChanged(pID));
    }

    @Override
    public void notifyMarks(int marks, int idHitten, int idShooter) {
        runLater(()->gui.notifyMarks(marks,idHitten,idShooter));
    }

    @Override
    public void notifyGrabCardAmmo(int pID) {
        runLater(()->gui.notifyGrabCardAmmo(pID));
    }

    @Override
    public void notifyRespawn(int pID) {
        runLater(()->gui.notifyRespawn(pID));
    }

    @Override
    public void chooseWeaponToGrab(List<CardWeapon> weapons) {
        runLater(()->gui.chooseWeaponToGrab(weapons));
    }

    @Override
    public void rejoinGamePhase(List<String> otherPlayers) {
        runLater(()->gui.rejoinGamePhase(otherPlayers));
    }

    @Override
    public void chooseRoomPhase(List<WaitingRoom> waitingRooms) {
        runLater(()->gui.chooseRoomPhase(waitingRooms));
    }

    @Override
    public void showMapsPhase(List<GameMap> availableMaps) {
        runLater(()->gui.showMapsPhase(availableMaps));
    }

    @Override
    public void reloadWeaponPhase(List<CardWeapon> weaponsToReload) {
        runLater(()->gui.reloadWeaponPhase(weaponsToReload));
    }

    @Override
    public void showReloadMessage(CardWeapon cW) {
        runLater(()->gui.showReloadMessage(cW));
    }

    @Override
    public void chooseWeaponToShoot(List<CardWeapon> myWeapons) {
        runLater(()->gui.chooseWeaponToShoot(myWeapons));
    }

    @Override
    public void chooseFirstEffect(FullEffect baseEff, FullEffect altEff) {
        runLater(()->gui.chooseFirstEffect(baseEff,altEff));
    }

    @Override
    public void usePlusBeforeBase(FullEffect plusEff) {
        runLater(()->gui.usePlusBeforeBase(plusEff));
    }

    @Override
    public void usePlusInOrder(List<FullEffect> plusEffects) {
        runLater(()->gui.usePlusInOrder(plusEffects));
    }

    @Override
    public void choosePlusEffect(List<FullEffect> plusEffects) {
        runLater(()->gui.choosePlusEffect(plusEffects));
    }

    @Override
    public void notifyPlayerSuspended(Player p) {
        runLater(()->gui.notifyPlayerSuspended(p));
    }

    @Override
    public void timeOutPhase() {
        runLater(gui::timeOutPhase);
    }

    @Override
    public void alreadyLoggedPhase() {
        runLater(gui::alreadyLoggedPhase);
    }

    @Override
    public void loginCompletedPhase() {
        runLater(gui::loginCompletedPhase);
    }

    @Override
    public void rejoinGameConfirm() {
        runLater(gui::rejoinGameConfirm);
    }

    @Override
    public void notifyPlayerRejoin(Player p) {
        runLater(()->gui.notifyPlayerRejoin(p));
    }

    @Override
    public void notifyPlayerLeavedWaitingRoom(Player p) {
        runLater(()->gui.notifyPlayerLeavedWaitingRoom(p));
    }

    @Override
    public void notifyPlayerJoinedWaitingRoom(Player p) {
        runLater(()->gui.notifyPlayerJoinedWaitingRoom(p));
    }

    @Override
    public void setController(ClientController clientController) {
        runLater(()->gui.setController(clientController));
    }

    @Override
    public void waitStart() {
        runLater(gui::waitStart);
    }

    @Override
    public void chooseConnection() {
        runLater(gui::chooseConnection);
    }

    @Override
    public void notifyConnectionError() {
        //TODO
    }

    @Override
    public void chooseCounterAttack(List<CardPower> counterattack, Player shooter) {

    }
}
