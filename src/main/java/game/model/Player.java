package game.model;

import game.model.exceptions.InsufficientAmmoException;
import game.model.exceptions.MapOutOfLimitException;
import game.model.exceptions.NoCardWeaponSpace;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Player implements Target{
    private PlayerColor color;
    private List<PlayerColor> marks;
    private List<PlayerColor> thisTurnMarks;
    private int id;
    private List<PlayerColor> damage;
    private AdrenalineLevel adrenaline;
    private List<CardWeapon> weapons;
    private CardWeapon actualWeapon;
    private List<Color> ammo;
    private List<CardPower> cardPower;
    private int deaths;
    private int points;
    private Square position;
    private Game game;
    private boolean isDead;
    private static final int MARKS_PER_ENEMY=3;

    public Player(int id, PlayerColor color)
    {
        this.id = id;
        this.color = color;
        this.adrenaline = AdrenalineLevel.NONE;
        this.deaths = 0;
        this.points = 0;
        this.isDead = false;
        this.marks = new ArrayList<>();
        this.thisTurnMarks = new ArrayList<>();
        this.damage = new ArrayList<>();
    }

    public PlayerColor getColor() {
        return color;
    }

    public void setColor(PlayerColor color) {
        this.color = color;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CardWeapon getActualWeapon() { return actualWeapon; }

    public void setActualWeapon(CardWeapon actualWeapon) {
        this.actualWeapon = actualWeapon;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public List<PlayerColor> getMark() {
        return marks;
    }

    public List<PlayerColor> getDamage() {
        return damage;
    }

    public AdrenalineLevel getAdrenaline() {
        return adrenaline;
    }

    public void setAdrenaline(AdrenalineLevel adrenaline) {
        this.adrenaline = adrenaline;
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

    public int getPoints() {
        return points;
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        isDead = dead;
    }

    public List<PlayerColor> getThisTurnMarks() {
        return thisTurnMarks;
    }

    /**
     * Respawn the player after his death
     * @param discard Power-up Card choosen by the Player to select the respawn point
     */
    public void respawn(CardPower discard){
        this.damage.clear();
        this.adrenaline = AdrenalineLevel.NONE;
        this.position = game.getMap().respawnColor(discard.getMapColor());
        this.isDead = false;
    }

    /**
     * Add damage cause of an enemy's weapon effect
     * Marks from the same enemy are counted to calculate the damage to be applied
     * The adrenaline attribute is updated according to the total damage suffered
     * Manage deaths and rages adding new kills into the kill-board
     * @param shooter
     * @param damage
     */
    @Override
    public void addDamage(Player shooter, int damage) {
        int num;
        boolean isRage = false;
        Kill lastKill=null;
        List<PlayerColor> marksToBeRemoved = marks.stream().filter(m-> m == shooter.getColor()).collect(Collectors.toList());
        damage += marksToBeRemoved.size();
        marks.removeAll(marksToBeRemoved);

        /*for(int i=0;i<marks.size();i++){
            if(marks.get(i)==shooter.getColor()){
                damage++;
                marks.remove(i);
                i--;
            }
        }*/

        if(this.damage.size()<11) {
            for (int i = 0; i < damage; i++)
                this.damage.add(shooter.getColor());
            num = this.damage.size();
            if (num > 10) {
                this.deaths++;
                this.isDead = true;
                shooter.addThisTurnMarks(this, 1); //when the victim of a damage die, the shooter receive a marks from the dead player
                if (num > 11) {
                    isRage = true;
                }
                game.addKill(shooter, this, isRage);

            }
            else if (num > 5)
                this.adrenaline = AdrenalineLevel.SHOOTLEVEL;
            else if (num > 2)
                this.adrenaline = AdrenalineLevel.GRABLEVEL;
        }
        else if (this.damage.size()==11 && damage>0){
            lastKill = game.getLastKill(this);
            lastKill.setRage(true);
        }
    }

    /**
     * Add points to the Player
     * @param addP points to be added
     */
    public void addPoints(int addP){
        this.points += addP;
    }

    /**
     * Add marks to the current turn marks, saved there to avoid to be added as damage in case of composite effects
     * They will be added to the Player's effective marks at the end of the action
     * @param shooter
     * @param marks
     */
    @Override
    public void addThisTurnMarks(Player shooter, int marks) {
        if(checkMarksNumber(shooter,marks))
            for (int i = 0; i < marks; i++)
                this.thisTurnMarks.add(shooter.getColor());
    }

    /**
     * Verify if the shooter can apply a finite number of marks to the victim (any player can apply max 3 marks per enemy)
     * @param shooter
     * @param marks
     * @return
     */
    public boolean checkMarksNumber(Player shooter, int marks){
        int count=0;
        for(int i=0;i<this.marks.size();i++){
            if(this.marks.get(i)==shooter.getColor())
                count++;
        }

        return marks+count<MARKS_PER_ENEMY;
    }

    /**
     * Move the Player
     * @param numSquare movement amount
     * @param dir movement direction
     * @throws MapOutOfLimitException if this movement would put the Player outside the Map
     */
    @Override
    public void move(int numSquare, Direction dir) throws MapOutOfLimitException {
        Square newPos;
        switch (dir){
            case UP:
                newPos = game.getMap().getSquare(this.position.getX(),this.position.getY() - numSquare);
                break;
            case DOWN:
                newPos = game.getMap().getSquare(this.position.getX(),this.position.getY() + numSquare);
                break;
            case RIGHT:
                newPos = game.getMap().getSquare(this.position.getX() + numSquare,this.position.getY());
                break;
            default:
                newPos = game.getMap().getSquare(this.position.getX() - numSquare,this.position.getY());
        }
        this.position.removePlayer(this);
        this.position = newPos;
    }

    /**
     * Add the last marks to the Player's marks
     * This happens at the end of each action
     */
    public void updateMarks() {
        this.marks.addAll(thisTurnMarks);
        thisTurnMarks.clear();
    }

    /**
     * Check if Player p1 made damage to the current player before Player p2
     * @param p1
     * @param p2
     * @return
     */
    public boolean findFirstDamage(PlayerColor p1, PlayerColor p2){
        for(int i=0;i<damage.size();i++){
            if(damage.get(i)==p1){
                return true;
            }
            else if(damage.get(i)==p2)
                return false;
        }
        return false;
    }

    /**
     * Clear the content of the actual weapon parameters used during the current turn
     */
    public void rifleActualWeapon(){
        this.actualWeapon.getPreviousTargets().clear();
        this.actualWeapon.setLastTargetSquare(null);
        this.actualWeapon.setLastDirection(null);
    }

    /**
     * Remove the indicated ammos from the Player
     * @param cost
     */
    public void removeAmmo(List<Color> cost)
    {
        ammo.removeAll(cost);
    }

    /**
     * Remove the indicated power-up cards from the Player
     * @param cost
     */
    public void removePowerUp(List<CardPower> cost)
    {
        cardPower.removeAll(cost);
    }

    /**
     *
     * @param weapon
     * @param powerUp
     * @throws InsufficientAmmoException
     * @throws NoCardWeaponSpace
     */
    public void pickUpWeapon(CardWeapon weapon, List<CardPower> powerUp) throws InsufficientAmmoException, NoCardWeaponSpace {
        List <Color> tmp = new ArrayList<>();
        tmp = weapon.getPrice();
        removePowerUp(powerUp);
        if (weapons.size() == 3) throw new NoCardWeaponSpace();
        for(int i = 0; i < powerUp.size(); i++){
            tmp.remove(powerUp.get(i).getColor());
            powerUp.remove(i);
        }
        if(!ammo.contains(tmp)) throw new InsufficientAmmoException();
        else{
            for (Color ammor : tmp){
                ammo.remove(ammor);
            }
        }
        for(CardPower cp : powerUp){
            cardPower.add(cp);
        }

    }
}
