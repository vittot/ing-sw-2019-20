package game.controller;

public enum ServerState {
    WAITING_FOR_PLAYERS,
    WAITING_SPAWN,
    WAITING_TURN,
    WAITING_ACTION,
    WAITING_WEAPON,
    HANDLING_MOVEMENT,
    WAITING_RELOAD,
    HANDLING_GRAB;
}
