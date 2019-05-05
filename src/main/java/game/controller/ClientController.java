package game.controller;

import game.controller.commands.ServerMessageHandler;
import game.controller.commands.servercommands.*;

import java.io.IOException;

public class ClientController /*implements ServerMessageHandler */{
    // reference to networking layer
    private final Client client;
    private Thread receiver;

    //TODO: add the view

    public ClientController(Client client) {
        this.client = client;
    }


    public void start() {

        receiver = new Thread(
                //receive messages from the Server
        );
        receiver.start();
    }

/*
    public void run() throws IOException {
        //TODO: launch various phases on the view
        return;
    }

    // TODO: ------ ServerMessage handling

    @Override
    public void handle(CheckReloadResponse serverMsg) {
        return;

    }

    @Override
    public void handle(ChooseSingleActionRequest serverMsg) {

        return;
    }

    @Override
    public void handle(ChooseSquareRequest serverMsg) {
        return;

    }

    @Override
    public void handle(ChooseTargetRequest serverMsg) {
        return;

    }

    @Override
    public void handle(ChooseTurnActionRequest serverMsg) {
        return;

    }

    @Override
    public void handle(InvalidTargetResponse serverMsg) {
        return;

    }

    @Override
    public void handle(InvalidWeaponResponse serverMsg) {
        return;

    }

    @Override
    public void handle(NotifyDamageResponse serverMsg) {
        return;

    }

    @Override
    public void handle(NotifyDeathResponse serverMsg) {
        return;

    }

    @Override
    public void handle(NotifyEndGameResponse serverMsg) {
        return;

    }

    @Override
    public void handle(NotifyGrabResponse serverMsg) {
        return;

    }

    @Override
    public void handle(NotifyMovementResponse serverMsg) {
        return;

    }

    @Override
    public void handle(NotifyPowerUpUsageResponse serverMsg) {
        return;

    }

    @Override
    public void handle(PickUpAmmoResponse serverMsg) {
        return;

    }

    @Override
    public void handle(PickUpWeaponResponse serverMsg) {
        return;

    }

    @Override
    public void handle(RespawnRequest serverMsg) {
        return;

    }

    @Override
    public void handle(InsufficientAmmoResponse serverMsg) {
        return;

    }

    @Override
    public void handle(OperationCompletedResponse serverMsg) {
        return;

    }*/
}
