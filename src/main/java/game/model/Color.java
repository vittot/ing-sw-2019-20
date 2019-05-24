package game.model;

public enum Color {
    RED,
    BLUE,
    YELLOW,
    ANY;

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
