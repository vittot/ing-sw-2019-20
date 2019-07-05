package game.model;

import java.io.Serializable;
import java.util.List;

/**
 * Contains the final game ranking
 */
public class Ranking implements Serializable {

    /**
     * usernames ordered by points
     */
    private List<String> orderedNicknames;
    /**
     * ordered points
     */
    private List<Integer> orderedPoints;

    /**
     * Default constructor
     * @param orderedNicknames ordered nicknames
     * @param orderedPoints ordered points
     */
    public Ranking(List<String> orderedNicknames, List<Integer> orderedPoints) {
        this.orderedNicknames = orderedNicknames;
        this.orderedPoints = orderedPoints;
    }

    /**
     * Return the nicknames ordered by points
     * @return ordered nicknames
     */
    public List<String> getOrderedNicknames() {
        return orderedNicknames;
    }

    /**
     * Return the points in order
     * @return points
     */
    public List<Integer> getOrderedPoints() {
        return orderedPoints;
    }
}
