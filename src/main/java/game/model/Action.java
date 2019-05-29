package game.model;

public enum Action {
    MOVEMENT,
    SHOOT,
    GRAB,
    RELOAD,
    EXIT;

    public static boolean checkAction(Action action){
        if(action == Action.GRAB || action == Action.MOVEMENT || action == Action.SHOOT || action == Action.RELOAD)
            return true;
        return false;
    }
}

