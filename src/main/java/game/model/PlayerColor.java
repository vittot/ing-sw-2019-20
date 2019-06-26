package game.model;

public enum PlayerColor {
    YELLOW,
    PURPLE,
    GREY,
    BLUE,
    GREEN;


    @Override
    public String toString() {
        switch(this) {
            case YELLOW:
                return "Yellow";
            case BLUE:
                return "Blue";
            case GREY:
                return "Grey";
            case GREEN:
                return "Green";
            default:
                return "Purple";
        }
    }
}
