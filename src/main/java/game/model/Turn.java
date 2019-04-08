package game.model;

public class Turn {
    private Player currentPlayer;
    private int numOfActions;
    private int numOfMovs;
    private Action currentAction;

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
    public void newAction(Action action, int adrenalin){
        if (numOfActions == 0){}  //TODO throw exe
        numOfActions = numOfActions - 1;
        switch (action){
            case GRAB:
                if (adrenalin > 0 ){
                            numOfMovs = 2;
                }else
                    numOfMovs = 1;

            case SHOOT:
                if(adrenalin == 2){
                    numOfMovs = 1;
                }else
                    numOfMovs = 0;
            case MOVEMENT: numOfMovs = 3;
        }
    }
}
