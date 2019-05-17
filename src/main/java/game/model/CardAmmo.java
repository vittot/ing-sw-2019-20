package game.model;

import java.util.List;

public class CardAmmo {
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
}
