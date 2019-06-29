package game.model;

import game.model.effects.FullEffect;

import java.io.Serializable;
import java.util.Objects;

public class CardPower implements Serializable {
    int id;
    private String name;
    private String description;
    private Color color;    //card color (for respawn or to pay ammo)
    //private List<Color> price; //price for the use of the effect
    private boolean useWhenDamaged;
    private boolean useWhenAttacking;
    private FullEffect effect;
    private Direction lastDirection;

    public CardPower(int id, String name, String description, Color color, boolean useWhenDamaged, boolean useWhenAttacking, FullEffect effect) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.useWhenDamaged = useWhenDamaged;
        this.useWhenAttacking = useWhenAttacking;
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

    public Direction getLastDirection() {
        return lastDirection;
    }

    public void setLastDirection(Direction lastDirection) {
        this.lastDirection = lastDirection;
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

    public boolean isUseWhenDamaged() {
        return useWhenDamaged;
    }

    public boolean isUseWhenAttacking() {
        return useWhenAttacking;
    }

    public FullEffect getEffect() {
        return effect;
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

    /**
     * Return a string representation of the powerup card
     * @return
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Name: " + effect.getName() + "\nDescription: " + effect.getDescription() + "\nColor: " + color.toString() + "\nPrice:\n");

        for(Color c: effect.getPrice())
            sb.append(c.toString());

        if(effect.getPrice().isEmpty())
            sb.append("free");
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardPower cardPower = (CardPower) o;
        return id == cardPower.id &&
                useWhenDamaged == cardPower.useWhenDamaged &&
                color == cardPower.color &&
                Objects.equals(effect, cardPower.effect);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, color, useWhenDamaged, effect);
    }
}
