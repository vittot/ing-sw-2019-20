package game.model;

import java.util.List;

public class CardPower {
    private int type;
    private Color color;
    private List<Color> price;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public List<Color> getPrice() {
        return price;
    }

    public void setPrice(List<Color> price) {
        this.price = price;
    }
}
