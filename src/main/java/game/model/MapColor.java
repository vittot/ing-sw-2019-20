package game.model;

public enum MapColor {
    RED,
    BLUE,
    YELLOW,
    PURPLE,
    GREY,
    GREEN;

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
