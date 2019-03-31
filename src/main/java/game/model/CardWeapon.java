package game.model;

import java.util.List;

public class CardWeapon {
    private List<Color> price;
    private List<List<Color>> pricePlus;
    private List<Color> priceAlt;
    private List<List<Effect>> effectPlus;
    private List<Effect> effectAlt;
    private boolean plusBeforeBase;           //True if you can use optional effect before base effect
    private boolean plusOrder;                //True if you have to respect the order of the list
    private List<Player> lastTarget;
    private Square lastTargetSquare;
    private int lastDirection;
    private boolean loaded;

    public List<Color> getPrice() {
        return price;
    }

    public void setPrice(List<Color> price) {
        this.price = price;
    }

    public List<List<Color>> getPricePlus() {
        return pricePlus;
    }

    public void setPricePlus(List<List<Color>> pricePlus) {
        this.pricePlus = pricePlus;
    }

    public List<Color> getPriceAlt() {
        return priceAlt;
    }

    public void setPriceAlt(List<Color> priceAlt) {
        this.priceAlt = priceAlt;
    }

    public List<List<Effect>> getEffectPlus() {
        return effectPlus;
    }

    public void setEffectPlus(List<List<Effect>> effectPlus) {
        this.effectPlus = effectPlus;
    }

    public List<Effect> getEffectAlt() {
        return effectAlt;
    }

    public void setEffectAlt(List<Effect> effectAlt) {
        this.effectAlt = effectAlt;
    }

    public boolean isPlusBeforeBase() {
        return plusBeforeBase;
    }

    public void setPlusBeforeBase(boolean plusBeforeBase) {
        this.plusBeforeBase = plusBeforeBase;
    }

    public boolean isPlusOrder() {
        return plusOrder;
    }

    public void setPlusOrder(boolean plusOrder) {
        this.plusOrder = plusOrder;
    }

    public List<Player> getLastTarget() {
        return lastTarget;
    }

    public void setLastTarget(List<Player> lastTarget) {
        this.lastTarget = lastTarget;
    }

    public Square getLastTargetSquare() {
        return lastTargetSquare;
    }

    public void setLastTargetSquare(Square lastTargetSquare) {
        this.lastTargetSquare = lastTargetSquare;
    }

    public int getLastDirection() {
        return lastDirection;
    }

    public void setLastDirection(int lastDirection) {
        this.lastDirection = lastDirection;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
