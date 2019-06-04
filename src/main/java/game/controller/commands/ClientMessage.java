package game.controller.commands;

import game.model.exceptions.InsufficientAmmoException;
import game.model.exceptions.MapOutOfLimitException;
import game.model.exceptions.NoCardAmmoAvailableException;

import java.io.Serializable;

public interface ClientMessage extends Serializable {
     ServerMessage handle(ClientMessageHandler handler) throws NoCardAmmoAvailableException, InsufficientAmmoException, MapOutOfLimitException;
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
