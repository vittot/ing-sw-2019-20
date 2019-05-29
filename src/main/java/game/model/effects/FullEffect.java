package game.model.effects;

import game.model.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FullEffect implements Serializable {
    private List<SimpleEffect> simpleEffects;
    private String name;
    private String description;
    private List<Color> price;
    private boolean beforeBase;

    public FullEffect(List<SimpleEffect> simpleEffects, String name, String description, List<Color> price, boolean beforeBase) {
        this.simpleEffects = simpleEffects;
        this.name = name;
        this.description = description;
        this.price = price;
        this.beforeBase = beforeBase;
    }
    public FullEffect(){
        this.simpleEffects = new ArrayList<>();
    }

    public void setPrice(List<Color> price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isBeforeBase() {
        return beforeBase;
    }

    public void setBeforeBase(boolean beforeBase) {
        this.beforeBase = beforeBase;
    }

    public List<Color> getPrice() {
        return price;
    }

    public FullEffect(String name, String description) {
        this.name = name;
        this.description = description;
        this.simpleEffects = new ArrayList<>();
    }

    public void addSimpleEffect(SimpleEffect se)
    {
        simpleEffects.add(se);
    }

    public List<SimpleEffect> getSimpleEffects() {
        return simpleEffects;
    }

    public SimpleEffect getSimpleEffect(int n)
    {
        return simpleEffects.get(n);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

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

    private boolean priceEquals(List<Color> price1, List<Color> price2)
    {
        if(price1 == null && price2 == null) return  true;
        return price1.size() == price2.size() && price1.containsAll(price2) && price2.containsAll(price1);
    }

    @Override
    public int hashCode() {
        return Objects.hash(simpleEffects, name, description, price, beforeBase);
    }



}
