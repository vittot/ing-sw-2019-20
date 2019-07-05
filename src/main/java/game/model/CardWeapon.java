package game.model;

import game.model.effects.FullEffect;
import game.model.effects.SimpleEffect;
import game.model.exceptions.InsufficientAmmoException;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * weapon card
 */
public class CardWeapon implements Serializable {
    /**
     * Identifier of the card weapon
     */
    private int id;
    /**
     * name of the weapon
     */
    private String name;
    /**
     * Price of the weapon, list of color
     */
    private List<Color> price;
    /**
     * Base effect of the card
     */
    private FullEffect baseEffect;
    /**
     * alternative effect of the card
     */
    private FullEffect altEffect;
    /**
     * optional effect of the crad
     */
    private List<FullEffect> plusEffects;
    /**
     * flag to say if you can use a plus effect before the base effect
     */
    private boolean plusBeforeBase;           //True if you can use a plus effect before the base effect
    /**
     * flag to say if the plus effect have to be done in order
     */
    private boolean plusOrder;                //True if you have to respect the order of the list
    /**
     * List of previus target hitted
     */
    private List<Player> previousTargets;
    /**
     * Last square hitted
     */
    private Square lastTargetSquare;
    /**
     * last direction used
     */
    private Direction lastDirection;
    /**
     * flag to say if the weapon is loaded
     */
    private boolean loaded;
    /**
     * last shooter of the card
     */
    private Player shooter;

    /**
     * Constructor
     * @param name string
     * @param price list
     * @param baseEffect effect
     * @param effectPlus effect
     * @param altEffect effect
     * @param plusBeforeBase  flag
     * @param plusOrder flag
     */
    public CardWeapon(String name, List<Color> price, FullEffect baseEffect, List<FullEffect> effectPlus, FullEffect altEffect, boolean plusBeforeBase, boolean plusOrder) {
        this.name = name;
        this.price = price;
        this.baseEffect = baseEffect;
        this.plusEffects = effectPlus;
        this.altEffect = altEffect;
        this.plusBeforeBase = plusBeforeBase;
        this.plusOrder = plusOrder;
        this.loaded = true;
        this.previousTargets = new ArrayList<>();
    }

    /**
     * Constructor
     * @param lastDirection direction
     * @param lastTargetSquare last target
     */
    public CardWeapon(Direction lastDirection, Square lastTargetSquare){
        this.lastDirection = lastDirection;
        this.lastTargetSquare = lastTargetSquare;
    }

    /**
     * Contructor
     * @param price price
     */
    public CardWeapon(List<Color> price){
        this.price = price;
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
     * reload weapon with available ammo and chosen power up
     * @param powerUp
     * @throws InsufficientAmmoException
     */
    public void reloadWeapon(List<CardPower> powerUp) throws InsufficientAmmoException{
        List<CardPower> tmpPUPaid = new ArrayList<>();
        List<Color> tmp = new ArrayList<>(price);
        List<Color> tmpAmmoPaid = new ArrayList<>();
        CardPower toRemove;
        if(loaded)
            return;

        for(int i=0 ; i < powerUp.size() ; i++){
            if(tmp.remove(powerUp.get(i).getColor()))
                tmpPUPaid.add(powerUp.get(i));
        }
        for(int i=0 ; i < shooter.getAmmo().size() ; i++){
            if(tmp.remove(shooter.getAmmo().get(i)))
                tmpAmmoPaid.add(shooter.getAmmo().get(i));
        }
        if(tmp.isEmpty()){
            loaded = true;
            for (int i = 0 ; i < tmpAmmoPaid.size(); i++)
                shooter.getAmmo().remove(tmpAmmoPaid.get(i));
            for (int i = 0 ; i < tmpPUPaid.size(); i++)
                shooter.getCardPower().remove(tmpPUPaid.get(i));
        }
        else if(!tmp.isEmpty())
            throw new InsufficientAmmoException();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CardWeapon that = (CardWeapon) o;
        return id == that.id;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    //it doesnt check player dist 2 not visible
    public boolean checkweapon(Player p){
        List<Square> squaretmp = new ArrayList<>();
        if (this.plusBeforeBase)
            return true;
        if(this.getBaseEffect().getSimpleEffect(0).searchTarget(p) != null){
            return true;
        }
        if(this.getName().equals("VORTEX CANNON")){
            for(Target tg : this.getBaseEffect().getSimpleEffect(0).searchTarget(p))
                squaretmp.add((Square) tg);
            for(Square sq : squaretmp){
                if(!sq.getVisiblePlayers(1,2).isEmpty())
                    return true;
            }
        }
        return false;
    }

    /**
     * Gets Identifier of the card weapon.
     *
     * @return Value of Identifier of the card weapon.
     */
    public int getId() {
        return id;
    }

    /**
     * Sets new Identifier of the card weapon.
     *
     * @param id New value of Identifier of the card weapon.
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Sets new name of the weapon.
     *
     * @param name New value of name of the weapon.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets name of the weapon.
     *
     * @return Value of name of the weapon.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets Price of the weapon, list of color.
     *
     * @return Value of Price of the weapon, list of color.
     */
    public List<Color> getPrice() {
        return price;
    }

    /**
     * Sets new Price of the weapon, list of color.
     *
     * @param price New value of Price of the weapon, list of color.
     */
    public void setPrice(List<Color> price) {
        this.price = price;
    }

    /**
     * Gets optional effect of the crad.
     *
     * @return Value of optional effect of the crad.
     */
    public List<FullEffect> getPlusEffects() {
        return plusEffects;
    }

    /**
     * Gets alternative effect of the card.
     *
     * @return Value of alternative effect of the card.
     */
    public FullEffect getAltEffect() {
        return altEffect;
    }

    /**
     * Gets Base effect of the card.
     *
     * @return Value of Base effect of the card.
     */
    public FullEffect getBaseEffect() {
        return baseEffect;
    }

    /**
     * Gets flag to say if the plus effect have to be done in order.
     *
     * @return Value of flag to say if the plus effect have to be done in order.
     */
    public boolean isPlusOrder() {
        return plusOrder;
    }

    /**
     * Gets last shooter of the card.
     *
     * @return Value of last shooter of the card.
     */
    public Player getShooter() {
        return shooter;
    }

    /**
     * Gets List of previus target hitted.
     *
     * @return Value of List of previus target hitted.
     */
    public List<Player> getPreviousTargets() {
        return previousTargets;
    }

    /**
     * Sets new last shooter of the card.
     *
     * @param shooter New value of last shooter of the card.
     */
    public void setShooter(Player shooter) {
        this.shooter = shooter;
    }

    /**
     * Sets new flag to say if the weapon is loaded.
     *
     * @param loaded New value of flag to say if the weapon is loaded.
     */
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    /**
     * Gets flag to say if the weapon is loaded.
     *
     * @return Value of flag to say if the weapon is loaded.
     */
    public boolean isLoaded() {
        return loaded;
    }

    /**
     * Sets new last direction used.
     *
     * @param lastDirection New value of last direction used.
     */
    public void setLastDirection(Direction lastDirection) {
        this.lastDirection = lastDirection;
    }

    /**
     * Sets new List of previus target hitted.
     *
     * @param previousTargets New value of List of previus target hitted.
     */
    public void setPreviousTargets(List<Player> previousTargets) {
        this.previousTargets = previousTargets;
    }

    /**
     * Gets last direction used.
     *
     * @return Value of last direction used.
     */
    public Direction getLastDirection() {
        return lastDirection;
    }

    /**
     * Gets Last square hitted.
     *
     * @return Value of Last square hitted.
     */
    public Square getLastTargetSquare() {
        return lastTargetSquare;
    }

    /**
     * Sets new Last square hitted.
     *
     * @param lastTargetSquare New value of Last square hitted.
     */
    public void setLastTargetSquare(Square lastTargetSquare) {
        this.lastTargetSquare = lastTargetSquare;
    }
}
