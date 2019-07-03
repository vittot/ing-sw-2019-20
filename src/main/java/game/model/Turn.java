package game.model;

import game.controller.Configuration;
import game.model.exceptions.NoResidualActionAvaiableException;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class Turn {
    private Player currentPlayer;
    private int numOfActions;
    private int numOfMovs;  //TODO a cosa serve?
    private Action currentAction;   //TODO a cosa serve?
    private List <Action> actionList;
    private Game game;
    private Timer timer;
    private boolean afterFirstPlayer = false;
    private boolean finalFrenzy;
    private List <Player> lastTurnPlayed;
    private int nPlayedFinalFrenzy;

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

    public List<Action> getActionList() {
        return actionList;
    }

    public void setActionList(List<Action> actionList) {
        this.actionList = actionList;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public int getNumOfActions() {
        return numOfActions;
    }

    public int getNumOfMovs() {
        return numOfMovs;
    }

    public int getnPlayedFinalFrenzy() {
        return nPlayedFinalFrenzy;
    }

    public Action getCurrentAction() {
        return currentAction;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setNumOfActions(int numOfActions) {
        this.numOfActions = numOfActions;
    }

    public void setNumOfMovs(int numOfMovs) {
        this.numOfMovs = numOfMovs;
    }

    public void setCurrentAction(Action currentAction) {
        this.currentAction = currentAction;
    }

    /**
     *
     * @param player
     * @param finalFrezy
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

    public void newTurn(boolean finalFrezy)
    {
        newTurn(currentPlayer,finalFrezy);
    }

    public void stopTimer()
    {
        timer.cancel();
        //System.out.println("TIMER CANCELLED " + currentPlayer.getNickName());
    }

    /**
     * Set the current step to the given action
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
    public List<Action> newAction(Action action, AdrenalineLevel adrenaline) throws NoResidualActionAvaiableException {
        actionList.clear();
        currentAction = action;

        if (numOfActions == 0){
            throw new NoResidualActionAvaiableException();
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
