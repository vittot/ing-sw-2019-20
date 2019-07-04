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

/**
 * class that represents a single player in game
 */
public class Player implements Target, Serializable, Comparable<Player> {
    private PlayerColor color; /** field that specifies the player color */
    private List<PlayerColor> marks; /** list of all the marks received by the player during the game */
    private List<PlayerColor> thisTurnMarks; /** list of all the marks received during the current turn by the player */
    private int id; /** field that contains the id of the player in game */
    private String nickName; /** nickName chosen by the user to play the game */
    private List<PlayerColor> damage; /** list of all the damages received by the player during the game */
    private AdrenalineLevel adrenaline; /** field that indicates the player adrenaline state */
    private transient List<CardWeapon> weapons; /** list of weapons grabbed by the player */
    private CardWeapon actualWeapon; /** field representing the weapon actually used by the player */
    private CardPower actualCardPower; /** reference to the power-up card actually use by the player */
    private List<Color> ammo; /** list of ammos grabbed by the player */
    private List<CardPower> cardPower; /** list of power-up cards grabbed by the player */
    private int deaths; /** field that counts the player deaths */
    private transient int points; /** field that counts the player points */
    private Square position; /** reference to the square where the player is positioned */
    private transient Game game; /** reference to the game */
    private boolean isDead; /** field that indicates if the player is dead or alive */
    private static final int MARKS_PER_ENEMY=3; /** constant value that specify the max number of marks that every player can give to each of the others */
    private boolean serializeEverything; /** boolean value that permits to specify if the transient attribute have to been serialized in transmission */
    private transient PlayerObserver playerObserver; /** reference to the object that is in charge of notify the game events */
    private boolean suspended; /** boolean value that specifies if the player has been suspended from the game cause of connection lost */
    private int killboardpoints;
    private boolean beforeFrenzy = true; /** boolean that specifies if the player got some damages before final frenzy*/
    /**
     * construct a player object ready to start the game
     * @param id
     * @param color
     */
    public Player(int id, PlayerColor color)
    {
        this.id = id;
        this.color = color;
        this.adrenaline = AdrenalineLevel.NONE;
        this.deaths = 0;
        this.points = 0;
        this.killboardpoints = 0;
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

    /**
     * Player constructor
     * @param id
     * @param color
     * @param nick
     */
    public Player(int id, PlayerColor color, String nick)
    {
        this(id,color);
        this.nickName = nick;
    }

    /**
     * Copy constructor
     * @param or
     */
    public Player(Player or) {
        this.color = or.color;
        this.marks = or.marks;
        this.thisTurnMarks = or.thisTurnMarks;
        this.id = or.id;
        this.nickName = or.nickName;
        this.damage = or.damage;
        this.adrenaline = or.adrenaline;
        this.weapons = or.weapons;
        this.actualWeapon = or.actualWeapon;
        this.actualCardPower = or.actualCardPower;
        this.ammo = or.ammo;
        this.cardPower = or.cardPower;
        this.deaths = or.deaths;
        this.points = or.points;
        this.position = or.position;
        this.game = or.game;
        this.isDead = or.isDead;
        this.serializeEverything = or.serializeEverything;
        this.playerObserver = or.playerObserver;
        this.suspended = or.suspended;
    }

    /**
     * return if the player is suspended from the game
     * @return suspended
     */
    public boolean isSuspended() {
        return suspended;
    }

    /**
     * set playerObserver attribute
     * @param playerObserver
     */
    public void setPlayerObserver(PlayerObserver playerObserver) {
        this.playerObserver = playerObserver;
    }

    /**
     * set serializeEverything attribute
     * @param serializeEverything
     */
    public void setSerializeEverything(boolean serializeEverything) {
        this.serializeEverything = serializeEverything;
    }

    /**
     * set suspended attribute
     * @param suspended
     */
    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    /**
     * return the nickName attribute
     * @return nickname
     */
    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * Get points obtained by the killboard
     * @return killbaord points
     */
    public int getKillboardpoints() {
        return killboardpoints;
    }

    /**
     * Set the killboard points
     * @param killboardpoints
     */
    public void setKillboardpoints(int killboardpoints) {
        this.killboardpoints = killboardpoints;
    }

    /**
     * Return the player string reference
     * @return nickName
     */
    @Override
    public String returnName(){
        return this.nickName;
    }

    /**
     * return color attribute
     * @return color
     */
    public PlayerColor getColor() {
        return color;
    }

    /**
     * set color attribute
     * @param color
     */
    public void setColor(PlayerColor color) {
        this.color = color;
    }

    /**
     * return id attribute
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * set id attribute
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * return actualWeapon attribute
     * @return actualWeapon
     */
    public CardWeapon getActualWeapon() { return actualWeapon; }

    /**
     * set actualWeapon attribute
     * @param actualWeapon
     */
    public void setActualWeapon(CardWeapon actualWeapon) {
        this.actualWeapon = actualWeapon;
    }

    /**
     * return game reference
     * @return game
     */
    public Game getGame() {
        return game;
    }

    /**
     * set game reference
     * @param game
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * set points attribute
     * @param points
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * return marks list
     * @return marks
     */
    public List<PlayerColor> getMark() {
        return marks;
    }

    /**
     * return damage list
     * @return damage
     */
    public List<PlayerColor> getDamage() {
        return damage;
    }

    /**
     * return the adrenaline level
     * @return adrenaline
     */
    public AdrenalineLevel getAdrenaline() {
        return adrenaline;
    }

    /**
     * set adrenaline level
     * @param adrenaline
     */
    public void setAdrenaline(AdrenalineLevel adrenaline) {
        this.adrenaline = adrenaline;
    }

    /**
     * return the weapons list
     * @return weapons
     */
    public List<CardWeapon> getWeapons() {
        return weapons;
    }

    /**
     * set weapons list
     * @param weapons
     */
    public void setWeapons(List<CardWeapon> weapons) {
        this.weapons = weapons;
    }

    /**
     * return ammo list
     * @return
     */
    public List<Color> getAmmo() {
        return ammo;
    }

    /**
     * set ammo list
     * @param ammo
     */
    public void setAmmo(List<Color> ammo) {
        this.ammo = ammo;
    }

    /**
     * return power-up cards list
     * @return
     */
    public List<CardPower> getCardPower() {
        return cardPower;
    }

    /**
     * set power-up cards list
     * @param cardPower
     */
    public void setCardPower(List<CardPower> cardPower) {
        this.cardPower = cardPower;
    }

    /**
     * return deaths attribute
     * @return deaths
     */
    public int getDeaths() {
        return deaths;
    }

    /**
     * set deaths parameter
     * @param deaths
     */
    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    /**
     * return position reference
     * @return position
     */
    public Square getPosition() {
        return position;
    }

    /**
     * set position reference
     * @param position
     */
    public void setPosition(Square position) {
        this.position = position;
    }

    /**
     * return points attribute
     * @return points
     */
    public int getPoints() {
        return points;
    }

    /**
     * return if the player is dead
     * @return isDead
     */
    public boolean isDead() {
        return isDead;
    }

    /**
     * set the isDead attribute
     * @param dead
     */
    public void setDead(boolean dead) {
        isDead = dead;
    }

    /**
     * return thisTurnMarks list
     * @return thisTurnMarks
     */
    public List<PlayerColor> getThisTurnMarks() {
        return thisTurnMarks;
    }

    /**
     * return actualCardPower reference
     * @return actualCardPower
     */
    public CardPower getActualCardPower() {
        return actualCardPower;
    }

    /**
     * set actualCardPower reference
     * @param actualCardPower
     */
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
        /*if(this.cardPower == null)
            System.out.println("LE CARD POWER SONO NULL, COME DOVREI FARE AD AGGIUNGERLE?!");*/
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
        if(this.position != null)
            this.position.removePlayer(this);
        this.position = game.getMap().respawnColor(discard.getMapColor());
        this.position.addPlayer(this);
        this.isDead = false;
        if(game.getCurrentTurn().isFinalFrenzy()) //to count points correctly in final frenzy
            this.deaths = 3;
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
            int num=0;
            boolean isRage = false;
            Kill lastKill = null;
            List<PlayerColor> marksToBeRemoved = marks.stream().filter(m -> m == shooter.getColor()).collect(Collectors.toList());
            damage += marksToBeRemoved.size();
            marks.removeAll(marksToBeRemoved);

            if (this.damage.size() < 11) { //11
                for (int i = 0; i < damage; i++)
                    this.damage.add(shooter.getColor());
                num = this.damage.size();
                if (num > 10) { //10
                    this.deaths++;
                    this.isDead = true;
                    if (num > 11) {
                        isRage = true;
                        shooter.addThisTurnMarks(this, 1); //the shooter receive a mark in case of rage
                    }
                    if (game != null)
                        game.addThisTurnKill(shooter, this, isRage);

                } else if (num > 5)
                    this.adrenaline = AdrenalineLevel.SHOOTLEVEL;
                else if (num > 2)
                    this.adrenaline = AdrenalineLevel.GRABLEVEL;
            } else if (this.damage.size() == 11 && damage > 0) { //11
                lastKill = game.getLastKill(this);
                lastKill.setRage(true);
                shooter.addThisTurnMarks(this, 1); //the shooter receive a mark in case of rage
                if(game != null)
                    game.notifyRage(lastKill);
            }
            if (game != null)
                game.notifyDamage(this, shooter, damage, marksToBeRemoved.size());
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
     * control if the player can grab a weapon
     * @param cw
     * @return true/false
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
     * control if the player can use a specific weapon effect
     * @param effect
     * @return true/false
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
     * control if the player can reload an unloaded weapon
     * @param cw
     * @return true/false
     */
    public boolean canReloadWeapon(CardWeapon cw){
        List<Color> priceTmp;
        priceTmp = new ArrayList<>(cw.getPrice());
        return controlPayment(priceTmp);
    }

    /**
     * control if the player can pay an indicated price
     * @param price
     * @return true/false
     */
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

    /**
     * control if the player has to use power-up to pay a specific price or it isn't necessary
     * @param price
     * @return true/false
     */
    public boolean mustUsePowerUpsToPay(List<Color> price) {
        if (!price.isEmpty() && price != null) {
            if (ammo != null && !ammo.isEmpty()) {
                if (price.get(0) == Color.ANY)
                    return false;
                else {
                    for (int i = 0; i < ammo.size(); i++)
                        price.remove(ammo.get(i));
                    if (price.isEmpty())
                        return false;
                    else
                        return true;
                }
            }
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
        else if(!ammoToPay.isEmpty() && ammoToPay.get(0) == Color.ANY && powerUp != null && !powerUp.isEmpty())
            cardPower.remove(powerUp.get(0));

    }

    /**
     * control if the player can grab ammos or he has full ammos for each color
     * @param ammos
     * @return
     */
    public List<Color> controlGrabAmmo(List<Color> ammos){
        int nRed = 0, nBlue = 0, nYellow = 0;
        for(Color a : ammo)
            switch(a){
                case BLUE:
                    nBlue++;
                    break;
                case YELLOW:
                    nYellow++;
                    break;
                case RED:
                    nRed++;
                    break;
                default:
                    break;
            }
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
        return ammos;
    }

    /**
     * Pick up an ammo card from the current Player position
     * @return list containing power-up card grabbed
     * @throws NoCardAmmoAvailableException
     */
    public List<CardPower> pickUpAmmo()throws NoCardAmmoAvailableException {
        List<CardPower> powerups = new ArrayList<>();
        if (position.getCardAmmo() == null){
            throw new NoCardAmmoAvailableException();
        }
        if(position.getCardAmmo()!=null){
            List<Color> ammos = position.getCardAmmo().getAmmo();
            ammos = controlGrabAmmo(ammos);
            ammo.addAll(ammos);
            if(position.getCardAmmo().getCardPower() > 0){
                powerups = new ArrayList<>();
                for(int i=0; i<position.getCardAmmo().getCardPower() && cardPower.size() + i + 1 <= 3; i++)
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

    /**
     * notify the starting of a new turn
     */
    public void notifyTurn()
    {
        playerObserver.onTurnStart();
    }

    /**
     * notify points update
     */
    public void notifyPoints()
    {
        playerObserver.notifyPoints();
    }

    /**
     * Serialize the object, excluding the sensible Player data (powerups and weapons) if the serializeEverything flag is not high
     * @param oos
     * @throws IOException
     */
    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        /*if(serializeEverything) {
            //The cast to Serializable is necessary to avoid Sonar bug issues because List does not implement Serializable but all implementations of List (such as ArrayList) in effect implements Serializable
            //oos.writeObject((Serializable)cardPower);
            oos.writeObject((Serializable)weapons);
            oos.writeInt(points);
        }
        serializeEverything = false;*/
    }

    /**
     * De-serialize the object. If the serializeEverything flag is high it deserialize also  powerups and weapons
     * @param ois
     * @throws IOException
     */
    private void readObject(ObjectInputStream ois) throws IOException, ClassNotFoundException {
        ois.defaultReadObject();
        /*if(serializeEverything)
        {
            //cardPower = (List<CardPower>)ois.readObject();
            weapons = (List<CardWeapon>)ois.readObject();
            points = ois.readInt();
        }
        else{*/
            weapons = new ArrayList<>();
        //}
    }

    /**
     * Return a short string representation of player id and nickname
     * @return
     */
    public String toShortString()
    {
        return "Id: " + id + "\nNickname: " + nickName;
    }

    /**
     * produce a string player description
     * @return player string version
     */
    @Override
    public String toString() {
        return "Player{" +
                "id= " + id +
                ", nickName= '" + nickName +
                "', color=" + color +
                '}';
    }

    /**
     * compare two players and verify if they are equals
     * @param o
     * @return true/false
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id &&
                color == player.color;
    }

    /**
     * used by equals method
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(color,id);
    }

    /**
     * add new ammos to the player's ammo list
     * @param ammo
     */
    public void addAmmo(Color ammo) {
        this.ammo.add(ammo);
    }

    /**
     * control if the player has weapons unloaded and so to reload
     * @return list of weapons to reload
     */
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
        if(!suspended)
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

    }

    /**
     * permit the player to rejoin the game where he was suspended
     */
    void rejoin()
    {
        this.suspended = false;
        this.game.notifyPlayerRejoined(this);
    }

    /**
     * control if the player has less or more point than another player
     * @param p2
     * @return true/false
     */
    @Override
    public int compareTo(Player p2)
    {
        if(this.points == p2.getPoints())
            return p2.getKillboardpoints() - this.killboardpoints;
        return p2.getPoints() - this.points;
    }

    /**
     * Gets beforeFrenzy.
     *
     * @return Value of beforeFrenzy.
     */
    public boolean isBeforeFrenzy() {
        return beforeFrenzy;
    }

    /**
     * Sets new beforeFrenzy.
     *
     * @param beforeFrenzy New value of beforeFrenzy.
     */
    public void setBeforeFrenzy(boolean beforeFrenzy) {
        this.beforeFrenzy = beforeFrenzy;
    }
}
