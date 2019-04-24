package game.model;

import game.model.effects.Effect;

import java.util.List;

public class CardPower {
    String name;
    String description;
    private Color color;    //card color (for respawn or to pay ammo)
    private List<Color> price; //price for the use of the effect
    private boolean usedWhenDamaged;
    private List<Effect> effect;

    public CardPower(String name, String description, Color color, List<Color> price, boolean usedWhenDamaged, List<Effect> effect) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.price = price;
        this.usedWhenDamaged = usedWhenDamaged;
        this.effect = effect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    /**
     * Get Power-up respawn color
     * @return
     */
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
