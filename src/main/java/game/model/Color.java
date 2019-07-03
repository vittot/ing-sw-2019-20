package game.model;

/**
 * Color of the card
 */
public enum Color {
    RED,
    BLUE,
    YELLOW,
    ANY;

    /**
     *
     * @return the color of the enum
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
            default:
                return "ANY";
        }
    }
}
