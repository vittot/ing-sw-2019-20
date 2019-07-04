package game.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Identify the card ammo
 */
public class CardAmmo implements Serializable {
    /**
     * List of color in the ammo card
     */
    private List<Color> ammo;
    /**
     * number of card power in the card
     */
    private int cardPower;

    /**
     * Constructor
     * @param ammo list of color
     * @param cardPower numebr od power up
     */
    public CardAmmo(List<Color> ammo, int cardPower) {
        this.ammo = ammo;
        this.cardPower = cardPower;
    }

    /**
     * Set the ammo card color
     * @param ammo list of color
     */
    public void setAmmo(List<Color> ammo) {
        this.ammo = ammo;
    }

    /**
     *
     * @return the list of color in the card
     */
    public List<Color> getAmmo() {
        return ammo;
    }

    /**
     *
     * @return the number of power up in the card
     */
    public int getCardPower() {
        return cardPower;
    }

    /**
     * check if the card power is the same with the given one
     * @param o card power given
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardAmmo cardAmmo = (CardAmmo) o;
        return cardPower == cardAmmo.cardPower &&
                Objects.equals(ammo, cardAmmo.ammo);
    }

    /**
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(ammo, cardPower);
    }
}
