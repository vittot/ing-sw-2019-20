package game.model;

import game.model.effects.FullEffect;

import java.io.Serializable;
import java.util.Objects;

/**
 * Identify the card power
 */
public class CardPower implements Serializable {
    /**
     * Unique id for the card
     */
    private int id;
    /**
     * name of the card
     */
    private String name;
    /**
     * description of the card
     */
    private String description;
    /**
     * Color of the card
     */
    private Color color;
    /**
     * used when damadeg flag
     */
    private boolean useWhenDamaged;
    /**
     * Use when attacking flag
     */
    private boolean useWhenAttacking;
    /**
     * Effect of the card power
     */
    private FullEffect effect;
    /**
     * Last Direction used of the effect
     */
    private Direction lastDirection;
    /**
     * last target hitted of the effect
     */
    private Player lastTarget;

    /**
     * Constructor
     * @param id id of the card
     * @param name name
     * @param description description
     * @param color color
     * @param useWhenDamaged flag
     * @param useWhenAttacking flag
     * @param effect effect
     */
    public CardPower(int id, String name, String description, Color color, boolean useWhenDamaged, boolean useWhenAttacking, FullEffect effect) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.color = color;
        this.useWhenDamaged = useWhenDamaged;
        this.useWhenAttacking = useWhenAttacking;
        this.effect = effect;
    }

    /**
     *
     *  @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * set the id
     * @param id int
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name set the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return last direction
     */
    public Direction getLastDirection() {
        return lastDirection;
    }

    /**
     * set last direction
     * @param lastDirection
     */
    public void setLastDirection(Direction lastDirection) {
        this.lastDirection = lastDirection;
    }

    /**
     *
     * @return last player
     */
    public Player getLastTarget() {
        return lastTarget;
    }

    /**
     * set last target
     * @param lastTarget
     */
    public void setLastTarget(Player lastTarget) {
        this.lastTarget = lastTarget;
    }

    /**
     *
     * @return color of the power-up
     */
    public Color getColor() {
        return color;
    }

    /**
     * set the color of the power up
     * @param color
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     *
     * @return the when damaged flag
     */
    public boolean isUseWhenDamaged() {
        return useWhenDamaged;
    }

    /***
     *
     * @return the when attacking flag
     */
    public boolean isUseWhenAttacking() {
        return useWhenAttacking;
    }

    /**
     *
     * @return the effect
     */
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

    /**
     * Check if the power up are the same
     * @param o
     * @return
     */
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

    /**
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, color, useWhenDamaged, effect);
    }
}
