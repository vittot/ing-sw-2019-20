package game.model;

import java.util.List;

public class CardAmmo {
    private List<Color> ammo;
    private CardPower cardPower;

    public void setAmmo(List<Color> ammo) {
        this.ammo = ammo;
    }

    public List<Color> getAmmo() {
        return ammo;
    }

    public CardPower getCardPower() {
        return cardPower;
    }

    public void setCardPower(CardPower cardPower) {
        this.cardPower = cardPower;
    }
}
