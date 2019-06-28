package game.controller;

public enum ClientState {
    WAITING_GRAB_WEAPON,
    WAITING_GRAB_AMMO,
    WAITING_SHOOT,
    WAITING_ACTION,
    GAME_END, WAITING_SPAWN, WAITING_START, TIMED_OUT;
}
