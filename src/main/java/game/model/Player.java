package game.model;

import java.util.List;

public class Player implements Target{
    private PlayerColor color;
    private List<PlayerColor> marks;
    private int givenMarks;
    private int id;
    private List<PlayerColor> damage;
    private AdrenalineLevel adrenalin;
    private List<CardWeapon> weapons;
    private int actualWeapon; //index of the weapon that the player is using
    private List<Color> ammo;
    private List<CardPower> cardPower;
    private int deaths;
    private Square position;
    private Game game;



    public Player(int id)
    {
        this.id = id;
    }

    public PlayerColor getColor() {
        return color;
    }

    public void setColor(PlayerColor color) {
        this.color = color;
    }

    public int getGivenMarks() {
        return givenMarks;
    }

    public void setGivenMarks(int givenMarks) {
        this.givenMarks = givenMarks;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActualWeapon() {
        return actualWeapon;
    }

    public void setActualWeapon(int actualWeapon) {
        this.actualWeapon = actualWeapon;
    }

    public List<PlayerColor> getMark() {
        return marks;
    }

    public void setMark(List<PlayerColor> mark) {
        this.marks = mark;
    }

    public List<PlayerColor> getDamage() {
        return damage;
    }

    public void setDamage(List<PlayerColor> damage) {

        this.damage = damage;
    }

    public AdrenalineLevel getAdrenalin() {
        return adrenalin;
    }

    public void setAdrenalin(AdrenalineLevel adrenalin) {
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
