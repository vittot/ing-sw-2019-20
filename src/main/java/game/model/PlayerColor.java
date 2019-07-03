package game.model;

/**
 * enum class that groups the five possible colors for the player in game
 */
public enum PlayerColor {
    YELLOW,
    PURPLE,
    GREY,
    BLUE,
    GREEN;


    /**
     * return the string conversion of an enum value
     * @return string version
     */
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
