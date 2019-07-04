package game.model;

/**
 * enum class representing the different map color identifying the different rooms
 */
public enum MapColor {
    RED,
    BLUE,
    YELLOW,
    PURPLE,
    GREY,
    GREEN;

    /**
     * convert the enum values in the corresponding string identification
     * @return
     */
    @Override
    public String toString() {
        switch(this) {
            case RED:
                return "RED";
            case YELLOW:
                return "YELLOW";
            case BLUE:
                return "BLUE";
            case GREY:
                return "GREY";
            case GREEN:
                return "GREEN";
            default:
                return "PURPLE";
        }
    }

}
