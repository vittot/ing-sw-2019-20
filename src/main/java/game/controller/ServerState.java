package game.controller;

/**
 * Indicate the server state during comunication with client
 */
public enum ServerState {
    JUST_LOGGED,
    WAITING_FOR_PLAYERS,
    WAITING_SPAWN,
    WAITING_TURN,
    WAITING_ACTION,
    WAITING_WEAPON,
    HANDLING_MOVEMENT,
    WAITING_RELOAD,
    HANDLING_GRAB,
    WAITING_RESPAWN,
    HANDLING_SHOOT,
    WAITING_POWER_USAGE,
    WAITING_POWER_TERMINATE,
    GAME_ENDED
}
