package game.controller.commands.servercommands;

import game.controller.commands.ServerGameMessage;
import game.controller.commands.ServerGameMessageHandler;
import game.controller.commands.ServerMessageHandler;

/**
 * notify update of points
 */
public class NotifyPoints implements ServerGameMessage {

    private int points; /** player total points */

    /**
     * construct correct message
     * @param points
     */
    public NotifyPoints(int points) {
        this.points = points;
    }

    /**
     * return points value
     * @return points
     */
    public int getPoints() {
        return points;
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
