package game.model;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class ScoreBoard {
    private List<Integer> playerPoints;
    private List<Integer> countPoints;
    private List<PlayerColor> colorPlayer;
    private final static  List<Integer> points;
    private List <Player> players;

    public ScoreBoard(int numPlayer, List<Player> players) {
        playerPoints = new ArrayList<Integer>();
        colorPlayer = new ArrayList<PlayerColor>();
        for (int i = 0; i<numPlayer; i++){
            playerPoints.add(0);
            countPoints.add(0);
            colorPlayer = add(players.get(i).getColor());
        }
        this.players = players;
    }

    static {
        points = new ArrayList<Integer>();
        points.add(8);
        points.add(6);
        points.add(4);
        points.add(2);
        points.add(1);
        points.add(1);
    }
    //TODO controllare gestione pareggi nel danno
    public void updatePonits (Player killed){
        int numDeaths, max;                           //max numero danni massimi tra i giocatori
        int indexPoints = 0;
        numDeaths = killed.getDeaths();
        for(int i = 0; i < killed.getDamage().size(); i++){
            indexPoints = colorPlayer.indexOf(killed.getDamage().get(i));
            countPoints.set(indexPoints,countPoints.get(indexPoints) + 1);
        }
        countPoints.set()                                                       //TODO FistBlood
        for(int i = 0; i < players.size(); i++){
            max = Collections.max(countPoints);
            if(max != 0) {
                indexPoints = countPoints.indexOf(max);
                playerPoints.set(indexPoints, playerPoints.get(indexPoints) + points.get(numDeaths));
                numDeaths = numDeaths + 1;
                countPoints.set(indexPoints, 0);
            }

    }

}
