package game.model;

import game.controller.GameManager;
import game.controller.PlayerObserver;
import game.model.effects.FullEffect;
import game.model.exceptions.InsufficientAmmoException;
import game.model.exceptions.MapOutOfLimitException;
import game.model.exceptions.NoCardWeaponSpaceException;
import game.model.exceptions.NoCardAmmoAvailableException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Player implements Target, Serializable, Comparable<Player> {
    private PlayerColor color;
    private List<PlayerColor> marks;
    private List<PlayerColor> thisTurnMarks;
    private int id;
    private String nickName;
    private List<PlayerColor> damage;
    private AdrenalineLevel adrenaline;
    private transient List<CardWeapon> weapons;
    private CardWeapon actualWeapon;
    private CardPower actualCardPower;
    private List<Color> ammo;
    private transient List<CardPower> cardPower;
    private int deaths;
    private transient int points;
    private Square position;
    private transient Game game;
    private boolean isDead;
    private static final int MARKS_PER_ENEMY=3;
    private boolean serializeEverything;
    private transient PlayerObserver playerObserver;
    private boolean suspended;

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
        this.serializeEverything = false;
        this.weapons = new ArrayList<>();
        this.cardPower = new ArrayList<>();
        this.ammo = new ArrayList<>();
        this.suspended = false;
    }

    public Player(int id, PlayerColor color, String nick)
    {
        this(id,color);
        this.nickName = nick;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setPlayerObserver(PlayerObserver playerObserver) {
        this.playerObserver = playerObserver;
    }

    public boolean isSerializeEverything() {
        return serializeEverything;
    }

    public void setSerializeEverything(boolean serializeEverything) {
        this.serializeEverything = serializeEverything;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String returnName(){
        return this.nickName;
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

    public CardPower getActualCardPower() {
        return actualCardPower;
    }

    public void setActualCardPower(CardPower actualCardPower) {
        this.actualCardPower = actualCardPower;
    }

    /**
     * Add a weapon to the player's hand
     * @param cw
     */
    public void addWeapon(CardWeapon cw)
    {
        this.weapons.add(cw);
    }

    /**
     * Add a power-up card to the player's hand
     * @param cp
     */
    public void addCardPower(CardPower cp)
    {
        CardPower alreadyPresent = this.cardPower.stream().filter(c -> c.getId() == cp.getId()).findFirst().orElse(null);
        if(alreadyPresent == null)
            this.cardPower.add(cp);
    }

    /**
     * Respawn the player after his death
     * @param discard Power-up Card choosen by the Player to select the respawn point
     */
    public void respawn(CardPower discard){
        this.damage.clear();
        this.adrenaline = AdrenalineLevel.NONE;
        this.position = game.getMap().respawnColor(discard.getMapColor());
        this.position.addPlayer(this);
        this.isDead = false;
        game.notifyRespawn(this);
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
        if(!this.equals(shooter) && damage > 0) {
            int num;
            boolean isRage = false;
            Kill lastKill = null;
            List<PlayerColor> marksToBeRemoved = marks.stream().filter(m -> m == shooter.getColor()).collect(Collectors.toList());
            damage += marksToBeRemoved.size();
            marks.removeAll(marksToBeRemoved);

        /*for(int i=0;i<marks.size();i++){
            if(marks.get(i)==shooter.getColor()){
                damage++;
                marks.remove(i);
                i--;
            }
        }*/

            if (this.damage.size() < 11) {
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

                } else if (num > 5)
                    this.adrenaline = AdrenalineLevel.SHOOTLEVEL;
                else if (num > 2)
                    this.adrenaline = AdrenalineLevel.GRABLEVEL;
            } else if (this.damage.size() == 11 && damage > 0) {
                lastKill = game.getLastKill(this);
                lastKill.setRage(true);
            }
            if (game != null)
                game.notifyDamage(this, shooter, damage);
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
        if(checkMarksNumber(shooter,marks) && shooter.getId() != this.id && marks > 0) {
            for (int i = 0; i < marks; i++)
                this.thisTurnMarks.add(shooter.getColor());
            if(game!=null)
                game.notifyMarks(this,shooter,marks);
        }

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
     * @throws MapOutOfLimitException if this movement would put the Player outside the GameMap
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
        move(newPos);
    }

    /**
     * Move the player in the indicated Square, notifying the game
     * @param s
     */
    public void move(Square s)
    {
        this.position.removePlayer(this);
        s.addPlayer(this);
        game.notifyMove(this);
    }

    /**
     * Add the last marks to the Player's marks
     * This happens at the end of each action
     */
    public void updateMarks() {
        this.marks.addAll(thisTurnMarks);
        thisTurnMarks.clear();
        if(game != null)
            game.notifyUpdateMarks(this);
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
        if(this.actualWeapon != null)
        {
            this.actualWeapon.getPreviousTargets().clear();
            this.actualWeapon.setLastTargetSquare(null);
            this.actualWeapon.setLastDirection(null);
        }

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
     *  PickUp weapon and pay the price in ammo/power up
     * @param weapon
     * @param powerUp
     * @throws InsufficientAmmoException
     * @throws NoCardWeaponSpaceException
     */
    public void pickUpWeapon(CardWeapon weapon, CardWeapon weaponToWaste, List<CardPower> powerUp) throws InsufficientAmmoException, NoCardWeaponSpaceException {
        List <Color> tmp = new ArrayList<>(weapon.getPrice());
        List <CardPower> tmpPU = new ArrayList<>();
        if(this.weapons.size()== 3)
            if(weaponToWaste==null)
                throw new NoCardWeaponSpaceException();
        if(tmp.size() > 1)
        {
            tmp = tmp.subList(1, tmp.size());
            pay(tmp, powerUp);
        }
        if (weapons.size() == 3){
            this.position.getWeapons().add(weaponToWaste);
            this.weapons.remove(weaponToWaste);
        }
        this.weapons.add(weapon);
        weapon.setShooter(this);
        this.position.getWeapons().remove(weapon);
        if(game != null)
            game.notifyGrabWeapon(this,weapon, weaponToWaste);

    }

    /**
     *
     * @param cw
     * @return
     */
    public boolean canGrabWeapon(CardWeapon cw){
        List<Color> priceTmp;
        if(cw.getPrice().size()>1) {
            priceTmp = new ArrayList<>(cw.getPrice().subList(1, cw.getPrice().size()));
            return controlPayment(priceTmp);
        }
        else
            return true;
    }

    /**
     *
     * @param effect
     * @return
     */
    public boolean canUseWeaponEffect(FullEffect effect){
        List<Color> priceTmp;
        if(effect.getPrice() != null){
            priceTmp = new ArrayList<>(effect.getPrice());
            return controlPayment(priceTmp);
        }
        return true;
    }

    /**
     *
     * @param cw
     * @return
     */
    public boolean canReloadWeapon(CardWeapon cw){
        List<Color> priceTmp;
        priceTmp = new ArrayList<>(cw.getPrice());
        return controlPayment(priceTmp);
    }

    public boolean controlPayment(List<Color> price){
        if(!price.isEmpty()) {
            if (price.get(0) == Color.ANY)
                return true;
            if (ammo != null && !ammo.isEmpty())
                for (int i = 0; i < ammo.size(); i++)
                    price.remove(ammo.get(i));
            if (price.isEmpty())
                return true;
            else if (cardPower != null && !cardPower.isEmpty())
                for (int i = 0; i < cardPower.size(); i++) {
                    price.remove(cardPower.get(i).getColor());
                }
            else
                return false;
            if (price.isEmpty())
                return true;
            else
                return false;
        }
        return true;
    }

    public boolean mustUsePowerUpsToPay(List<Color> price) {
        if (!price.isEmpty() && price != null) {
            if (price.get(0) == Color.ANY)
                return true;
            if (ammo != null && !ammo.isEmpty())
                for (int i = 0; i < ammo.size(); i++)
                    price.remove(ammo.get(i));
            if (price.isEmpty())
                return false;
            else
                return true;
        }
        return false;
    }

    /**
     * Pay a cost with ammo and eventually powerups
     * @param ammoToPay
     * @param powerUp
     * @throws InsufficientAmmoException
     */
    public void pay(List<Color> ammoToPay, List<CardPower> powerUp) throws InsufficientAmmoException {
        List <Color> tmp = null;
        List <CardPower> tmpPU = null;
        if(ammoToPay != null && !ammoToPay.isEmpty() && ammoToPay.get(0)!=Color.ANY) {
            tmp = new ArrayList<>(ammoToPay);
            tmpPU = new ArrayList<>();
            if (powerUp != null && !powerUp.isEmpty())
                for (int i = 0; i < powerUp.size(); i++) {
                    if (tmp.contains(powerUp.get(i).getColor())) {
                        tmp.remove(powerUp.get(i).getColor());
                        tmpPU.add(powerUp.get(i));
                    }
                }
            if (!ammo.containsAll(tmp)) throw new InsufficientAmmoException();
            else {
                for (Color ammor : tmp) {
                    ammo.remove(ammor);
                }
                for (CardPower cp : tmpPU) {
                    cardPower.remove(cp);
                }
            }
        }
    }

    /**
     * Pick an ammo from the current Player position
     */
    public List<CardPower> pickUpAmmo()throws NoCardAmmoAvailableException {
        List<CardPower> powerups = new ArrayList<>();
        if (position.getCardAmmo() == null){
            throw new NoCardAmmoAvailableException();
        }
        if(position.getCardAmmo()!=null){
            List<Color> ammos = position.getCardAmmo().getAmmo();
            int nRed = 0, nBlue = 0, nYellow = 0;
            List<Color> toRemove = new ArrayList<>();
            for(Color c: ammos)
            {
                if(c == Color.BLUE) {
                    if(nBlue >= 3)
                        toRemove.add(c);
                    nBlue++;
                }
                else if(c == Color.RED)
                {
                    if(nRed >= 3)
                        toRemove.add(c);
                    nRed++;
                }
                else if(c == Color.YELLOW)
                {
                    if(nYellow >= 3)
                        toRemove.add(c);
                    nYellow++;
                }
            }
            ammos.removeAll(toRemove);
            ammo.addAll(ammos);
            if(position.getCardAmmo().getCardPower() > 0){
                powerups = new ArrayList<>();
                for(int i=0; i<position.getCardAmmo().getCardPower(); i++)
                    powerups.add(game.drawPowerUp());
                cardPower.addAll(powerups);
            }
            game.notifyGrabCardAmmo(this,ammos);
            position.setCardAmmo(null);
        }
        return powerups;
    }

    /**
     * Notify its serverController to respawn the player
     */
    public void notifyRespawn()
    {
        playerObserver.onRespawn();
    }

    public void notifyTurn()
    {
        playerObserver.onTurnStart();
    }

    /**
     * Serialize the object, excluding the sensible Player data (powerups and weapons) if the serializeEverything flag is not high
     * @param oos
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        if(serializeEverything) {
            //The cast to Serializable is necessary to avoid Sonar bug issues because List does not implement Serializable but all implementations of List (such as ArrayList) in effect implements Serializable
            oos.writeObject((Serializable)cardPower);
            oos.writeObject((Serializable)weapons);
            oos.writeInt(points);
        }
        serializeEverything = false;
    }

    /**
     * De-serialize the object. If the serializeEverything flag is high it deserialize also  powerups and weapons
     * @param ois
     * @throws IOException
     */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        if(serializeEverything)
        {
            cardPower = (List<CardPower>)ois.readObject();
            weapons = (List<CardWeapon>)ois.readObject();
            points = ois.readInt();
        }
        else{
            weapons = new ArrayList<>();
        }
    }

    /**
     * Return a short string representation of player id and nickname
     * @return
     */
    public String toShortString()
    {
        return "Id: " + id + "\nNickname: " + nickName;
    }

    @Override
    public String toString() {
        return "Player{" +
                "id= " + id +
                ", nickName= '" + nickName +
                "', color=" + color +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id &&
                color == player.color;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color,id);
    }

    public void addAmmo(Color ammo) {
        this.ammo.add(ammo);
    }

    public List<CardWeapon> hasToReload() {
        List<CardWeapon> toReload = new ArrayList<>();
        for (CardWeapon cw : weapons)
            if(!cw.isLoaded())
                toReload.add(cw);
        if(toReload.isEmpty())
            return null;
        return toReload;
    }

    /**
     * Suspend the player and notify the other players
     */
    public void suspend(boolean timeOut)
    {
        this.suspended = true;
        GameManager.get().suspendPlayer(this);

        if(!timeOut)
            this.game.getCurrentTurn().stopTimer();
        if(!this.game.isEnded())
        {   this.game.notifyPlayerSuspended(this, timeOut);
            if(this.game.getNumPlayersAlive() < 3)
            {
                this.game.endGame();
            }
        }
        this.playerObserver.onSuspend(timeOut);
    }

    public void rejoin()
    {
        this.suspended = false;
        this.game.notifyPlayerRejoined(this);
    }

    @Override
    public int compareTo(Player p2)
    {
        if(this.points == p2.getPoints())
            return this.id - p2.getId();
        return p2.getPoints() - this.points;
    }
}
