package game.model;

import java.util.List;

public class Player {
    private Color color;
    private int mark;
    private List<Color> damage;
    private int adrenalin;
    private List<CardWeapon> weapons;
    private List<Color> ammo;
    private List<CardPower> cardPower;
    private int deaths;
    private Square position;


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getMark() {
        return mark;
    }

    public void setMark(int mark) {
        this.mark = mark;
    }

    public List<Color> getDamage() {
        return damage;
    }

    public void setDamage(List<Color> damage) {
        this.damage = damage;
    }

    public int getAdrenalin() {
        return adrenalin;
    }

    public void setAdrenalin(int adrenalin) {
        this.adrenalin = adrenalin;
    }

    public List<CardWeapon> getWeapons() {
        return weapons;
    }

    public void setWeapons(List<CardWeapon> weapons) {
        this.weapons = weapons;
    }

    public List<Color> getAmmo() {
        return ammo;
    }

    public void setAmmo(List<Color> ammo) {
        this.ammo = ammo;
    }

    public List<CardPower> getCardPower() {
        return cardPower;
    }

    public void setCardPower(List<CardPower> cardPower) {
        this.cardPower = cardPower;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public Square getPosition() {
        return position;
    }

    public void setPosition(Square position) {
        this.position = position;
    }
}
