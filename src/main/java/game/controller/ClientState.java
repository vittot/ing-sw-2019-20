package game.controller;

public enum ClientState {                   //Used from GUI
    CHOOSEWEAPONTOWASTE,
    CHOOSEWEAPONTOGRAB,
    CHOOSESTEP,
    CHOOSEACTIOIN,
    CHOOSEWEAPONTOSHOOT,
    WAITING_GRAB_WEAPON,                    //Used from Client Context
    WAITING_GRAB_AMMO,
    WAITING_SHOOT,
    WAITING_ACTION,
    GAME_END, WAITING_SPAWN, WAITING_START, TIMED_OUT;
}
