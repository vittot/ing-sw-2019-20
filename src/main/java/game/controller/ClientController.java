package game.controller;

import game.controller.commands.ServerMessageHandler;
import game.controller.commands.servercommands.*;

import java.io.IOException;

public class ClientController implements ServerMessageHandler {
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


    public void run() throws IOException {
        //TODO: launch various phases on the view
    }

    // TODO: ------ ServerMessage handling

    @Override
    public void handle(CheckReloadResponse serverMsg) {

    }

    @Override
    public void handle(ChooseSingleActionRequest serverMsg) {

    }

    @Override
    public void handle(ChooseSquareRequest serverMsg) {

    }

    @Override
    public void handle(ChooseTargetRequest serverMsg) {

    }

    @Override
    public void handle(ChooseTurnActionRequest serverMsg) {

    }

    @Override
    public void handle(InvalidTargetResponse serverMsg) {

    }

    @Override
    public void handle(InvalidWeaponResponse serverMsg) {

    }

    @Override
    public void handle(NotifyDamageResponse serverMsg) {

    }

    @Override
    public void handle(NotifyDeathResponse serverMsg) {

    }

    @Override
    public void handle(NotifyEndGameResponse serverMsg) {

    }

    @Override
    public void handle(NotifyGrabResponse serverMsg) {

    }

    @Override
    public void handle(NotifyMovementResponse serverMsg) {

    }

    @Override
    public void handle(NotifyPowerUpUsageResponse serverMsg) {

    }

    @Override
    public void handle(PickUpAmmoResponse serverMsg) {

    }

    @Override
    public void handle(PickUpWeaponResponse serverMsg) {

    }

    @Override
    public void handle(RespawnRequest serverMsg) {

    }

    @Override
    public void handle(InsufficientAmmoResponse serverMsg) {

    }

    @Override
    public void handle(OperationCompletedResponse serverMsg) {

    }
}
