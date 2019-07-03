package game.model;

import game.model.exceptions.MapOutOfLimitException;

/**
 * interface that represent the behavioral of the possible target in game
 */
public interface Target {

    /**
     * return the target name
     * @return
     */
    public String returnName();

    /**
     * add damage from the specified shooter
     * @param shooter
     * @param damage
     */
    public void addDamage(Player shooter, int damage);

    /**
     * add marks from the specified shooter
     * @param shooter
     * @param marks
     */
    public void addThisTurnMarks(Player shooter, int marks);

    /**
     * move the target in a direction for a finite number of square
     * @param numSquare
     * @param dir
     * @throws MapOutOfLimitException
     */
    public void move(int numSquare, Direction dir) throws MapOutOfLimitException;
}
