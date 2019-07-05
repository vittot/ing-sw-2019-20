package game.model.effects;

import game.model.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * class representing a whole weapon effect, it is composed by a list of simple effect that realize all the step necessary to complete it
 */
public class FullEffect implements Serializable {
    /** list of simple effects each of which composes the full effect */
    private List<SimpleEffect> simpleEffects;
    /** name of the effect */
    private String name;
    /** description of the effect */
    private String description;
    /** list of color representing the price to pay to use the effect */
    private List<Color> price;
    /** boolean value that indicate if the effect can be used before than a base effect */
    private boolean beforeBase;

    /**
     * construct a full effect with the correct parameters
     * @param simpleEffects
     * @param name
     * @param description
     * @param price
     * @param beforeBase
     */
    public FullEffect(List<SimpleEffect> simpleEffects, String name, String description, List<Color> price, boolean beforeBase) {
        this.simpleEffects = simpleEffects;
        this.name = name;
        this.description = description;
        this.price = price;
        this.beforeBase = beforeBase;
    }

    /**
     * construct an empty full effect object
     */
    public FullEffect(){
        this.simpleEffects = new ArrayList<>();
    }

    /**
     * set the price of the effect
     * @param price
     */
    public void setPrice(List<Color> price) {
        this.price = price;
    }

    /**
     * sete the name of the effect
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * set the effect description
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * return if the effect can be used before than a base effect
     * @return
     */
    public boolean isBeforeBase() {
        return beforeBase;
    }

    /**
     * set the beforeBase attribute
     * @param beforeBase
     */
    public void setBeforeBase(boolean beforeBase) {
        this.beforeBase = beforeBase;
    }

    /**
     * return the price of the effect
     * @return
     */
    public List<Color> getPrice() {
        return price;
    }

    /**
     * construct a simply full effect object
     * @param name
     * @param description
     */
    public FullEffect(String name, String description) {
        this.name = name;
        this.description = description;
        this.simpleEffects = new ArrayList<>();
    }

    /**
     * add a new simple effect object to the corresponding list
     * @param se
     */
    public void addSimpleEffect(SimpleEffect se)
    {
        simpleEffects.add(se);
    }

    /**
     * return the simple effects list
     * @return simpleEffects
     */
    public List<SimpleEffect> getSimpleEffects() {
        return simpleEffects;
    }

    /**
     * return a specific simple effect contained in the list
     * @param n
     * @return simpleEffects.get(n)
     */
    public SimpleEffect getSimpleEffect(int n)
    {
        return simpleEffects.get(n);
    }

    /**
     * return the effect name
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * return the effect description
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * verify if two full effect objects are equals
     * @param o
     * @return true/false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FullEffect that = (FullEffect) o;
        return beforeBase == that.beforeBase &&
                Objects.equals(simpleEffects, that.simpleEffects) &&
                Objects.equals(name, that.name) &&
                Objects.equals(description, that.description) &&
                priceEquals(price, that.price);
    }

    /**
     * verify if two prices are equals
     * @param price1
     * @param price2
     * @return true/false
     */
    private boolean priceEquals(List<Color> price1, List<Color> price2)
    {
        if(price1 == null && price2 == null) return  true;
        return price1.size() == price2.size() && price1.containsAll(price2) && price2.containsAll(price1);
    }

    /**
     * used by the equals method
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(simpleEffects, name, description, price, beforeBase);
    }



}
