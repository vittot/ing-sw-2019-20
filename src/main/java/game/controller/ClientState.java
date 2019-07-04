package game.controller;

/**
 * Used for client state during interation with player choice, used from client controller and gui
 */
public enum ClientState {                   //Used from GUI
    CHOOSEWEAPONTOWASTE,
    CHOOSEWEAPONTOGRAB,
    CHOOSESTEP,
    CHOOSEACTIOIN,
    CHOOSEWEAPONTOSHOOT,
    CHOOSEPBB,
    CHOOSEFIRSTEFFECT,
    CHOOSEPLUSORDER,
    CHOOSEPLUSEFFECT,
    CHOOSERELOAD,
    CHOOSECOUNTER,
    CHOOSECARDPOWER,
    CHOOSESCOPE,
    WAITING_GRAB_WEAPON,
    WAITING_GRAB_AMMO,
    WAITING_SHOOT,
    WAITING_ACTION,
    GAME_END,
    WAITING_SPAWN,
    WAITING_START,
    TIMED_OUT,
    HANDLING_MOVEMENT, RECONNECTING, WAITING_TURN, WAITING_FINAL_RELOAD, WAITING_COUNTERATTACK;
}
