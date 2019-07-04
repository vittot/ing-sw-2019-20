package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

/**
 * notify a completed movement action
 */
public class NotifyMovement implements ServerGameMessage {
    private int id; /** id of the player that complete the movement **/
    private int x; /** x coordinate of the movement destination */
    private int y; /** y coordinate of the movement destination */

    /**
     * construct correct message
     * @param id
     * @param x
     * @param y
     */
    public NotifyMovement(int id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    /**
     * return player id
     * @return id
     */
    public int getId() {
        return id;
    }

    /**
     * return x coordinate
     * @return x
     */
    public int getX() {
        return x;
    }

    /**
     * return y coordinate
     * @return y
     */
    public int getY() {
        return y;
    }

    /**
     * Handle the message
     * @param handler who handle the messag
     * @param handler
     */
    @Override
    public void handle(ServerGameMessageHandler handler) {
        handler.handle(this);
    }

    /**
     * Handle the message
     * @param handler who handle the messag
     * @param handler
     */
    @Override
    public void handle(ServerMessageHandler handler) {
        handler.handle(this);
    }
}
