package game.model.effects;

import game.model.Color;

import java.util.ArrayList;
import java.util.List;

public class FullEffect {
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
}
