package game.model;

import game.controller.Configuration;
import game.model.exceptions.NoResidualActionAvaiableException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * class that represent the state of the current turn of the game
 */
public class Turn {
    private Player currentPlayer; /** field that reference the player who is actually play the turn */
    private int numOfActions; /** field that permit to count the remaining turn action */
    private int numOfMovs;  /** field that which contains the number of movement made during the current turn  */
    private Action currentAction;   /** field that reference the current action chosen from the player */
    private List <Action> actionList; /** field that contains all the possible steps the player can make cause of the chosen action */
    private Game game; /** field that reference the game */
    private Timer timer; /** field that reference the timer object that allow the disconnection of the current player cause of a long time inactivity */
    private boolean afterFirstPlayer = false; /** field that recognize if the current player is playing after or before the player that starts the game */
    private List <Player> lastTurnPlayed;

    /**
     * construct a turn of a specific game
     * @param currentPlayer
     * @param game
     */
    public Turn(Player currentPlayer, Game game)
    {
        this.currentPlayer = currentPlayer;
        this.game = game;
        actionList = new ArrayList<>();
        numOfActions = 2;
    }

    /**
     * return the actionList attribute
     * @return actionList
     */
    public List<Action> getActionList() {
        return actionList;
    }

    /**
     * set the attribute actionList
     * @param actionList
     */
    public void setActionList(List<Action> actionList) {
        this.actionList = actionList;
    }

    /**
     * return the currentPlayer attribute
     * @return currentPlayer
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * return the numOfActions attribute
     * @return numOfActions
     */
    public int getNumOfActions() {
        return numOfActions;
    }

    /**
     * return the numOfMovs attribute
     * @return numOfMovs
     */
    public int getNumOfMovs() {
        return numOfMovs;
    }

    /**
     * return the currentAction attribute
     * @return currentAction
     */
    public Action getCurrentAction() {
        return currentAction;
    }

    /**
     * set the currentPlayer attribute
     * @param currentPlayer
     */
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    /**
     * set the numOfActions attribute
     * @param numOfActions
     */
    public void setNumOfActions(int numOfActions) {
        this.numOfActions = numOfActions;
    }

    /**
     * provide the creation and good starting of a new turn
     * @param player , finalFrezy
     */
    public void newTurn(Player player, boolean finalFrezy){

        game.getPlayers().forEach(Player::updateMarks);

        currentPlayer = player;

        if(finalFrezy && player.equals(game.getFirstPlayerToPlay())){
            this.afterFirstPlayer = true;
        }
        if(afterFirstPlayer)
            lastTurnPlayed.add(player);

        if (finalFrezy && afterFirstPlayer) {
            numOfMovs = 0;
            numOfActions = 1;
        }
        if(finalFrezy && !afterFirstPlayer){
            numOfMovs = 0;
            numOfActions = 2;
        }
        numOfActions = 2;
        numOfMovs = 0;
        startTimer();
        game.notifyTurnChange(currentPlayer);
    }

    /**
     * start the timer
     */
    public void startTimer()
    {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                System.out.println("TIMER SCADUTO! " + currentPlayer.getNickName());
                currentPlayer.suspend(true);
            }
        };
        timer.schedule(task, Configuration.TURN_TIMER_MS);
    }

    /**
     * allow the management of a new turn creation
     * @param finalFrezy
     */
    public void newTurn(boolean finalFrezy)
    {
        newTurn(currentPlayer,finalFrezy);
    }

    /**
     * method that stop the timer
     */
    public void stopTimer()
    {
        timer.cancel();
        System.out.println("TIMER CANCELLED " + currentPlayer.getNickName());
    }

    /**
     * set the current step to the given action
     * @param ac
     * @return
     */
    public boolean applyStep(Action ac){
        //timer.cancel();
        System.out.println("TIMER CANCELLED" + currentPlayer.getNickName()) ;
        int i = 0;
        currentPlayer.rifleActualWeapon();
        game.getPlayers().forEach(Player::updateMarks);
        if(ac.equals(Action.MOVEMENT))
            this.numOfMovs++;
        if(this.actionList.contains(ac)){
            i = actionList.indexOf(ac);
            setActionList(actionList.subList(i+1,actionList.size()));
            //startTimer();
            return true;
        }
        //startTimer();
        return false;
    }

    /**
     * set a list of sub-action you can do from a selected Action(GRAB/MOVE/SHOOT)
     * @param action
     * @param adrenaline
     */
    public List<Action> newAction(Action action, AdrenalineLevel adrenaline, boolean finalFreazy) throws NoResidualActionAvaiableException {
        actionList.clear();
        currentAction = action;

        if (numOfActions == 0){
            throw new NoResidualActionAvaiableException();
        }
        numOfActions = numOfActions - 1;
        if(finalFreazy) {
            if (this.lastTurnPlayed.contains(currentPlayer)){
                switch (action) {
                    case GRAB:
                        actionList.add(Action.MOVEMENT);
                        actionList.add(Action.MOVEMENT);
                        actionList.add(Action.MOVEMENT);
                        actionList.add(Action.GRAB);
                        break;
                    case SHOOT:
                        actionList.add(Action.MOVEMENT);
                        actionList.add(Action.MOVEMENT);
                        actionList.add(Action.RELOAD);
                        actionList.add(Action.SHOOT);
                        break;
                }
            }else {
                switch (action) {
                    case GRAB:
                        actionList.add(Action.MOVEMENT);
                        actionList.add(Action.MOVEMENT);
                        actionList.add(Action.GRAB);
                        break;
                    case SHOOT:
                        actionList.add(Action.MOVEMENT);
                        actionList.add(Action.RELOAD);
                        actionList.add(Action.SHOOT);
                        break;
                    case MOVEMENT:
                        actionList.add(Action.MOVEMENT);
                        actionList.add(Action.MOVEMENT);
                        actionList.add(Action.MOVEMENT);
                        actionList.add(Action.MOVEMENT);
                        break;
                }
            }
        }
        else {
            switch (action) {
                case GRAB:
                    if (adrenaline == AdrenalineLevel.GRABLEVEL || adrenaline == AdrenalineLevel.SHOOTLEVEL) {
                        actionList.add(Action.MOVEMENT);
                        actionList.add(Action.MOVEMENT);
                        actionList.add(Action.GRAB);

                    } else {
                        actionList.add(Action.MOVEMENT);
                        actionList.add(Action.GRAB);
                    }
                    break;

                case SHOOT:
                    if (adrenaline == AdrenalineLevel.SHOOTLEVEL) {
                        actionList.add(Action.MOVEMENT);
                        actionList.add(Action.SHOOT);
                    } else {
                        actionList.add(Action.SHOOT);
                    }
                    break;
                case MOVEMENT:
                    actionList.add(Action.MOVEMENT);
                    actionList.add(Action.MOVEMENT);
                    actionList.add(Action.MOVEMENT);
            }
            return actionList;
        }
        return actionList;
    }

    
}
