package game.controller.commands;

import java.io.Serializable;

public interface ServerMessage extends Serializable {
    void handle(ServerMessageHandler handler);
    /*
        Scegli major azione(M/S/R)
        Scegli minor action.list
        Scegli possibili square(Movement)
        Scegli bersagli (arma, effetto)(Controllo armi e effetti corretti)
        Bersagli scelti validi  (Applica effetti dmg nel model)
        Bersagli scelti non validi
        Notifica danni (Observer)
        Notifica spostamento (Observer)
        Notifica morti/infierito/punti (Observer)
        Notifica raccolta (Observer)
        Notifica power up (Observer)
        Notifica fine partita (Observer)
        Richista Respawn (power up)

     */
}
