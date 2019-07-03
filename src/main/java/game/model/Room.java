package game.model;

import game.model.exceptions.MapOutOfLimitException;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * class that represents a map room, (set of square with same map color)
 */
public class Room implements Target, Serializable {

    private MapColor color; /** field that identifies the color of the room */
    private GameMap map; /** reference to the game map */

    /**
     * construct a room specifying the color and the map that contains it
     * @param color
     * @param map
     */
    public Room(MapColor color, GameMap map) {
        this.color = color;
        this.map = map;
    }

    /**
     * return the color attribute
     * @return color
     */
    public MapColor getColor() {
        return color;
    }

    /**
     * Get a List of the Square which compose the Room
     * @return
     */
    public List<Square> getSquares(){
        return map.getRoomSquares(color);
    }

    /**
     * For every Player in the Room add damage cause of an enemy's weapon effect
     * Marks from the same enemy are counted to calculate the damage to be applied
     * The adrenaline attribute is updated according to the total damage suffered
     * Manage deaths and rages adding new kills into the kill-board
     * @param shooter
     * @param damage
     */
    @Override
    public void addDamage(Player shooter, int damage) { getSquares().forEach( s -> s.addDamage(shooter,damage));
    }

    /**
     * For every Player in the Room add marks to the current turn marks, saved there to avoid to be added as damage in case of composite effects
     * They will be added to the Player's effective marks at the end of the action
     * @param shooter
     * @param marks
     */
    @Override
    public void addThisTurnMarks(Player shooter, int marks) {
        getSquares().forEach( s -> s.addThisTurnMarks(shooter,marks));
    }

    /**
     * Apply the movement effect to all Players in the room
     * @param numSquare movement amount
     * @param dir movement direction
     * @throws MapOutOfLimitException if the movement would put players outside of the GameMap
     */
    @Override
    public void move(int numSquare, Direction dir) throws MapOutOfLimitException {
        for(Square s : getSquares())
        {
            s.move(numSquare, dir);
        }
    }

    /**
     * Return the rooms string reference
     * @return
     */
    @Override
    public String returnName() {
        return null;
    }

    /**
     * describe the object in string version
     * @return description
     */
    @Override
    public String toString() {
        return "Room{" +
                "color=" + color +
                ", map=" + map +
                '}';
    }

    /**
     * compare two rooms and verify if their equals
     * @param o
     * @return
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Room room = (Room) o;
        return color == room.color;
    }

    /**
     * used by equals method
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(color);
    }
}
