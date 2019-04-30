package game.controller.commands;

import java.io.Serializable;

public interface ClientMessage extends Serializable {
     void handle(ClientMessageHandler handler);
      /*
        raccogli ammo -> power up
        raccogli arma
        Invia major azione scelta(movimento/spara/raccogli)
        Scegli action.list(movements/grab)
        Scegli square scelto(Movement)
        Spara(Arma e effetto)
        Ritorna berasgli scelti
        Richiesta carica (Arma, power up)
        Scelta respawn (power up scartata)
     */
}
