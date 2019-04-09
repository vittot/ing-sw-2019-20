package game.model;

import game.model.effects.Effect;

import java.util.List;

public class CardPower {
    private int type;
    private Color color;    //card color (for respwan or to pay ammo)
    private List<Color> price; //price for the use of the effect
    private boolean usedWhenDamaged;
    private List<Effect> effect;

    public CardPower(int type, Color color, List<Color> price, boolean usedWhenDamaged, List<Effect> effect) {
        this.type = type;
        this.color = color;
        this.price = price;
        this.usedWhenDamaged = usedWhenDamaged;
        this.effect = effect;
    }

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

    public MapColor getMapColor(){
        switch(this.color){
            case BLUE:
                return MapColor.BLUE;
            case RED:
                return MapColor.RED;
            case YELLOW:
                return MapColor.YELLOW;
            default:
                return null;

        }
    }
}
