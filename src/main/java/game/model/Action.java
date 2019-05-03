package game.model;

public enum Action {
    MOVEMENT,
    SHOOT,
    GRAB;

    public static boolean checkAction(Action action){
        if(action == Action.GRAB || action == Action.MOVEMENT || action == Action.SHOOT)
            return true;
        return false;
    }
}

