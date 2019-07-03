package game.model;

/**
 * Enum to identify the action
 */
public enum Action {
    MOVEMENT,
    SHOOT,
    GRAB,
    RELOAD,
    POWER,
    EXIT;

    /**
     *
     * @param action action selected
     * @return true if is one of the major action
     */
    public static boolean checkAction(Action action){
        if(action == Action.GRAB || action == Action.MOVEMENT || action == Action.SHOOT || action == Action.RELOAD)
            return true;
        return false;
    }
}

