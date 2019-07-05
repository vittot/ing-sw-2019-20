package game.model;

import game.controller.Configuration;
import game.model.exceptions.NoResidualActionAvailableException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * class that represent the state of the current turn of the game
 */
public class Turn {
    /** field that reference the player who is actually play the turn */
    private Player currentPlayer;
    /** field that permit to count the remaining turn action */
    private int numOfActions;
    /** field that which contains the number of movement made during the current turn  */
    private int numOfMovs;
    /** field that reference the current action chosen from the player */
    private Action currentAction;
    /** field that contains all the possible steps the player can make cause of the chosen action */
    private List <Action> actionList;
    /** field that reference the game */
    private Game game;
    /** field that reference the timer object that allow the disconnection of the current player cause of a long time inactivity */
    private Timer timer;
    /** field that recognize if the current player is playing after or before the player that starts the game */
    private boolean afterFirstPlayer = false;
    /** list of reference to player that played the last turns */
    private List <Player> lastTurnPlayed;
    /** count of many players played their final frenzy turn */
    private int nPlayedFinalFrenzy;
    /** field that specifies if it is a final frenzy turn */
    private boolean finalFrenzy;

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
        finalFrenzy = false;
        lastTurnPlayed = new ArrayList<>();
        nPlayedFinalFrenzy = 0;
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

    public int getnPlayedFinalFrenzy() {
        return nPlayedFinalFrenzy;
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
     * set numOfMovs attribute
     * @param numOfMovs
     */
    public void setNumOfMovs(int numOfMovs) {
        this.numOfMovs = numOfMovs;
    }

    /**
     * set currentAction attribute
     * @param currentAction
     */
    public void setCurrentAction(Action currentAction) {
        this.currentAction = currentAction;
    }

    /**
     * provide the creation and good starting of a new turn
     * @param player , finalFrezy
     */
    public void newTurn(Player player, boolean finalFrezy){

        game.getPlayers().forEach(Player::updateMarks);

        currentPlayer = player;

        this.finalFrenzy = finalFrezy;

        if(finalFrezy)
            this.nPlayedFinalFrenzy++;

        if(finalFrezy && player.equals(game.getFirstPlayerToPlay())){
            this.afterFirstPlayer = true;
        }
        if(afterFirstPlayer)
            lastTurnPlayed.add(player);

        if (finalFrezy && afterFirstPlayer) {
            numOfMovs = 0;
            numOfActions = 1;
        }
        else if(finalFrezy && !afterFirstPlayer){
            numOfMovs = 0;
            numOfActions = 2;
        }
        else {
            numOfActions = 2;
            numOfMovs = 0;
        }
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
        //System.out.println("TIMER CANCELLED " + currentPlayer.getNickName());
    }

    /**
     * set the current step to the given action
     * @param ac
     * @return
     */
    public boolean applyStep(Action ac){
        //timer.cancel();

        int i = 0;
        currentPlayer.rifleActualWeapon();
        game.getPlayers().forEach(Player::updateMarks);
        if(ac.equals(Action.MOVEMENT))
            this.numOfMovs++;
        if(this.actionList.contains(ac)){
            i = actionList.indexOf(ac);
            if(i == actionList.size()-1)
                actionList.clear();
            else
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
    public List<Action> newAction(Action action, AdrenalineLevel adrenaline) throws NoResidualActionAvailableException {
        actionList.clear();
        currentAction = action;

        if (numOfActions == 0){
            throw new NoResidualActionAvailableException();
        }
        numOfActions = numOfActions - 1;
        if(finalFrenzy) {
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


    /**
     * Return if the turn is the final frenzy turn
     * @return
     */
    public boolean isFinalFrenzy() {
        return finalFrenzy;
    }

    /**
     * Check if the "movement" turn action is allowed
     * @return
     */
    public boolean isMovAllowed() {
        if(finalFrenzy && lastTurnPlayed.contains(currentPlayer))
            return false;
        return true;
    }
}
