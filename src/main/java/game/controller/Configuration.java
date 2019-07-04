package game.controller;

/**
 * Class with timer values (here defaults, but if exists they are loaded by xml config file)
 */
public class Configuration {
    /**
     * Turn time
     */
    public static int TURN_TIMER_MS = 300000;
    /**
     * Time before game start after reaching 3 players in a waiting room
     */
    public static int WAITING_ROOM_TIMER_MS = 10000;
    /** Ping interval time */
    public static int PING_INTERVAL_MS = 10000;
    /** Ping waiting time */
    public static int PING_WAITING_TIME_MS = 4000;
}
