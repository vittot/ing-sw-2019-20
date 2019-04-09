package game.model;

import game.model.effects.Effect;


import java.util.List;


public class CardWeapon {
    private String name;
    private List<Color> price;
    private List<List<Color>> pricePlus;
    private List<Color> priceAlt;
    private List<Effect> baseEffect;
    private List<Effect> altEffect;
    private List<List<Effect>> plusEffects;
    private boolean plusBeforeBase;           //True if you can use optional effects before base effects
    private boolean plusOrder;                //True if you have to respect the order of the list
    private List<Player> lastTarget;
    private Square lastTargetSquare;
    private int lastDirection;
    private boolean loaded;

    public CardWeapon(String name, List<Color> price, List<List<Color>> pricePlus, List<Color> priceAlt, List<Effect> baseEffect, List<List<Effect>> effectPlus, List<Effect> altEffect, boolean plusBeforeBase, boolean plusOrder) {
        this.name = name;
        this.price = price;
        this.pricePlus = pricePlus;
        this.priceAlt = priceAlt;
        this.baseEffect = baseEffect;
        this.plusEffects = effectPlus;
        this.altEffect = altEffect;
        this.plusBeforeBase = plusBeforeBase;
        this.plusOrder = plusOrder;
        this.loaded = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public List<List<Effect>> getPlusEffects() {
        return plusEffects;
    }

    public void setPlusEffects(List<List<Effect>> plusEffects) {
        this.plusEffects = plusEffects;
    }

    public List<Effect> getAltEffect() {
        return altEffect;
    }

    public void setAltEffect(List<Effect> altEffect) {
        this.altEffect = altEffect;
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

    public void reloadWeapon(List<CardAmmo> cardAmmo, List<CardPower> powerUp){
        // TODO
    }
}
