package game.model;

public enum Direction{
    UP,
    DOWN,
    LEFT,
    RIGHT,
    ERROR;

    public static Direction getDirection(int var){
        switch(var){
            case 0:
                return Direction.UP;
            case 1:
                return Direction.RIGHT;
            case 2:
                return Direction.DOWN;
            default:
                return Direction.LEFT;

        }
    }
}
