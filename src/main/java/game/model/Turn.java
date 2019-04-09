package game.model;

import java.util.ArrayList;
import java.util.List;


public class Turn {
    private Player currentPlayer;
    private int numOfActions;
    private int numOfMovs;
    private Action currentAction;
    private List <Action> actionList;


    public List<Action> getActionList() {
        return actionList;
    }

    public void setActionList(List<Action> actionList) {
        this.actionList = actionList;
    }

    public Turn() {
        actionList = new ArrayList<Action>();
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
     * Get the next Square in the indicate Direction
     * @param player , finalFrezy
     * @return void
     */

    public void newTurn(Player player, boolean finalFrezy){
        
        currentPlayer = player;
        if (finalFrezy) {
            numOfMovs = 0;
            numOfActions = 2;
        }
        numOfActions = 2;
        numOfMovs = 0;
    }

    /**
     * set a list of sub-action you can do from a selected Action(GRAB/MOVE/SHOOT)
     * @param action
     * @param adrenaline
     */
    public void newAction(Action action, AdrenalineLevel adrenaline){
        actionList.clear();
        if (numOfActions == 0){}  //TODO throw exe
        numOfActions = numOfActions - 1;
        switch (action){
            case GRAB:
                if (adrenaline == AdrenalineLevel.GRABLEVEL || adrenaline == AdrenalineLevel.SHOOTLEVEL){
                            actionList.add(Action.MOVEMENT);
                            actionList.add(Action.MOVEMENT);
                            actionList.add(Action.GRAB);

                }else
                    actionList.add(Action.MOVEMENT);
                    actionList.add(Action.GRAB);

            case SHOOT:
                if(adrenaline == AdrenalineLevel.SHOOTLEVEL){
                    actionList.add(Action.MOVEMENT);
                    actionList.add(Action.GRAB);
                }else
                    actionList.add(Action.GRAB);
            case MOVEMENT:
                actionList.add(Action.MOVEMENT);
                actionList.add(Action.MOVEMENT);
                actionList.add(Action.MOVEMENT);
        }
    }
}
