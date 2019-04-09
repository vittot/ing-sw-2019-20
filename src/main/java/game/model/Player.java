package game.model;

import java.util.List;

public class Player implements Target{
    private PlayerColor color;
    private List<PlayerColor> marks;
    private List<PlayerColor> thisTurnMarks;
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

    /**
     *
     * @param thisTurnMarks
     */
    public void addThisTurnMarks(List<PlayerColor> thisTurnMarks) {
        this.thisTurnMarks.addAll(thisTurnMarks);
    }

    public void updateMarks() {
        this.marks.addAll(thisTurnMarks);
    }

    public List<PlayerColor> getDamage() {
        return damage;
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

    /**
     *
     * @param discard
     */
    public void respawn(CardPower discard){
        this.damage.clear();
        this.adrenalin = AdrenalineLevel.NONE;
        this.position = game.getMap().respawnColor(discard.getMapColor());
    }

    /**
     *
     * add damage cause of an enemy's weapon effect
     * also marks from the same enemy are counted to calculate the damage to be applied
     * the adrenaline attribute is updated according to the total damage suffered
     * manage deaths
     * @param shooter
     * @param damage
     */
    public void addDamage(Player shooter, List<PlayerColor> damage) {
        int num;
        boolean isRage = false;
        for(int i=0;i<marks.size();i++){
            if(marks.get(i)==damage.get(0)){
                damage.add(marks.get(i));
                marks.remove(i);
                i--;
            }
        }
        this.damage.addAll(damage);
        num = this.damage.size();
        if(num>10){
            this.deaths++;
            if(num>11){
                isRage = true;
            }
            game.addKill(shooter, this, isRage);
        }
        else if(num>5)
            this.adrenalin = AdrenalineLevel.SHOOTLEVEL;
        else if(num>2)
            this.adrenalin = AdrenalineLevel.GRABLEVEL;
    }

}
