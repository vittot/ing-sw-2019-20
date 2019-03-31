package game.model;

import java.util.List;

public class CardWeapon {
    private int price;
    private int pricePlus;
    private int priceAlt;
    private List<Effect> effectPlus;
    private Effect effectAlt;
    private boolean plusBeforeBase;
    private boolean plusOrder;
    private List<Player> lastTarget;
    private Square lastTargetSquare;
    private int lastDirection;
    private boolean loaded;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getPricePlus() {
        return pricePlus;
    }

    public void setPricePlus(int pricePlus) {
        this.pricePlus = pricePlus;
    }

    public int getPriceAlt() {
        return priceAlt;
    }

    public void setPriceAlt(int priceAlt) {
        this.priceAlt = priceAlt;
    }

    public List<Effect> getEffectPlus() {
        return effectPlus;
    }

    public void setEffectPlus(List<Effect> effectPlus) {
        this.effectPlus = effectPlus;
    }

    public Effect getEffectAlt() {
        return effectAlt;
    }

    public void setEffectAlt(Effect effectAlt) {
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
