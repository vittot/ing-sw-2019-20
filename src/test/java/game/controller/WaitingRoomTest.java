package game.controller;

import game.model.GameMap;
import game.model.Player;
import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class WaitingRoomTest {

    @Test
    void serializeTest() throws IOException, ClassNotFoundException {
        WaitingRoom w = new WaitingRoom(1,1,3);
        ServerController s = mock(ServerController.class);
        ServerController s2 = mock(ServerController.class);
        w.addWaitingPlayer(s, "aa");
        w.addWaitingPlayer(s2,"bb");

        ByteArrayOutputStream bo = new ByteArrayOutputStream();
        ObjectOutputStream oo = new ObjectOutputStream(bo);
        oo.writeObject(w);
        ByteArrayInputStream bi = new ByteArrayInputStream(bo.toByteArray());
        ObjectInputStream oi = new ObjectInputStream(bi);
        WaitingRoom wdes = (WaitingRoom) oi.readObject();

        boolean samePlayers = w.getPlayers().size() == wdes.getPlayers().size();
        for(int i=0;i<w.getPlayers().size();i++)
        {
            Player p1 = (Player)w.getPlayers().toArray()[i];
            Player p2 = wdes.getPlayers().stream().filter(p -> p.hashCode() == p1.hashCode()).findFirst().orElse(null);
            samePlayers = (p2 != null);
        }
        assertTrue(samePlayers);
    }

}