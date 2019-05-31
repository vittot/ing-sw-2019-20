package game.controller;

public interface PlayerObserver {

    void onRespawn();
    void onTurnStart();
    void onSuspend(boolean timeOut);
}
