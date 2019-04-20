package game.model;

import game.model.effects.Effect;
import game.model.exceptions.InsufficientAmmoException;


import java.util.ArrayList;
import java.util.List;


public class CardWeapon {
    private String name;
    private List<Color> price;
    private List<List<Color>> pricePlus;
    private List<Color> priceAlt;
    private List<Effect> baseEffect;
    private List<Effect> altEffect;
    private List<List<Effect>> plusEffects;
    private List<String> effectDescriptions;
    private boolean plusBeforeBase;           //True if you can use optional effects before base effects
    private boolean plusOrder;                //True if you have to respect the order of the list
    private List<Player> previousTargets;
    private Square lastTargetSquare;
    private Direction lastDirection;
    private boolean loaded;
    private Player shooter;

    public CardWeapon(String name, List<Color> price, List<List<Color>> pricePlus, List<Color> priceAlt, List<Effect> baseEffect, List<List<Effect>> effectPlus, List<Effect> altEffect, List<String> effectDescription, boolean plusBeforeBase, boolean plusOrder) {
        this.name = name;
        this.price = price;
        this.pricePlus = pricePlus;
        this.priceAlt = priceAlt;
        this.baseEffect = baseEffect;
        this.plusEffects = effectPlus;
        this.altEffect = altEffect;
        this.effectDescriptions = effectDescription;
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

    public List<Player> getPreviousTargets() { return previousTargets; }

    public void setPreviousTargets(List<Player> previousTargets) {
        this.previousTargets = previousTargets;
    }

    public Square getLastTargetSquare() {
        return lastTargetSquare;
    }

    public void setLastTargetSquare(Square lastTargetSquare) {
        this.lastTargetSquare = lastTargetSquare;
    }

    public Direction getLastDirection() {
        return lastDirection;
    }

    public void setLastDirection(Direction lastDirection) {
        this.lastDirection = lastDirection;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }


    /**
     * Get the last Player hit by this Weapon
     * @return
     */
    public Player getLastTarget()
    {
        return this.previousTargets.get(previousTargets.size() - 1);
    }

    /**
     *
     * @param ammo
     * @param powerUp
     * @throws InsufficientAmmoException
     */
    public void reloadWeapon(List<Color> ammo, List<CardPower> powerUp) throws InsufficientAmmoException{
        List<Color> tmp = new ArrayList<>();
        List<Color> colorPwrUp = new ArrayList<>();
        if(loaded)
            return;

        tmp.addAll(price);
        if (ammo.size() + powerUp.size() == price.size()) {
            if (tmp.containsAll(ammo))
                tmp.removeAll(ammo);

            if (!tmp.isEmpty()) {
                for (int i = 0; i < powerUp.size(); i++)
                    colorPwrUp.add(powerUp.get(i).getColor());
                if (tmp.containsAll(colorPwrUp))
                    tmp.removeAll(colorPwrUp);
            }

            if (tmp.isEmpty()) {
                loaded = true;
                //TODO removeAmmo in player
            }

        }
        if(!tmp.isEmpty())
            throw new InsufficientAmmoException();
    }
}
