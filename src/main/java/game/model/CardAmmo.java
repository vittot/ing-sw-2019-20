package game.model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class CardAmmo implements Serializable {
    private List<Color> ammo;
    private int cardPower; //number of cardpower to be taken with this card ammo

    public CardAmmo(List<Color> ammo, int cardPower) {
        this.ammo = ammo;
        this.cardPower = cardPower;
    }

    public void setAmmo(List<Color> ammo) {
        this.ammo = ammo;
    }

    public List<Color> getAmmo() {
        return ammo;
    }

    public int getCardPower() {
        return cardPower;
    }

    public void setCardPower(int cardPower) {
        this.cardPower = cardPower;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardAmmo cardAmmo = (CardAmmo) o;
        return cardPower == cardAmmo.cardPower &&
                Objects.equals(ammo, cardAmmo.ammo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ammo, cardPower);
    }
}
