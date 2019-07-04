package game.controller;

/**
 * Observer object for a player
 */
public interface PlayerObserver {

    /**
     * Request the player to respawn
     */
    void onRespawn();

    /**
     * Alert the player about the start of his turn
     */
    void onTurnStart();

    /**
     * Called on the player suspension
     * @param timeOut true if he has been suspended because of turn timeout
     */
    void onSuspend(boolean timeOut);

    /**
     * Notify about points update
     */
    void notifyPoints();
}
