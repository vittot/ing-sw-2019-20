package game.model;

import game.model.effects.FullEffect;

import java.io.Serializable;
import java.util.List;

public class CardPower implements Serializable {
    int id;
    private String name;
    private String description;
    private Color color;    //card color (for respawn or to pay ammo)
    private List<Color> price; //price for the use of the effect
    private boolean usedWhenDamaged;
    private FullEffect effect;

    public CardPower(int id, String name, String description, Color color, List<Color> price, boolean usedWhenDamaged, FullEffect effect) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.price = price;
        this.usedWhenDamaged = usedWhenDamaged;
        this.effect = effect;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: " + name + "\nDescription: " + description + "\nColor: " + color.toString() + "\nPrice:\n");

        for(Color c: price)
            sb.append(c.toString());

        if(price.isEmpty())
            sb.append("free");
        return sb.toString();
    }
}
