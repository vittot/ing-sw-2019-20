package game.controller;

import game.controller.commands.ServerGameMessage;
import game.model.Target;
import game.model.effects.*;

import java.util.List;

/**
 * Interface for the controller able to handle the different types of Effect
 */
public interface EffectHandler {

    /**
     * Handle the movement effect on the game
     * @param e effect
     * @return response
     */
    ServerGameMessage handle(MovementEffect e);

    /**
     * Handle the plain damage effect on the game
     * @param e effect
     * @return response
     */
    ServerGameMessage handle(PlainDamageEffect e);

    /**
     * Handle the square damage effect on the game
     * @param e effect
     * @return response
     */
    ServerGameMessage handle(SquareDamageEffect e);

    /**
     * Handle the room damage effect
     * @param e effect
     * @return response
     */
    ServerGameMessage handle(RoomDamageEffect e);

    /**
     * Handle the area damage effect
     * @param e effect
     * @return response
     */
    ServerGameMessage handle(AreaDamageEffect e);

    /**
     * Handle the target selection for a movement effect, proceeding to the selection of positions
     * @param movementEffect effect
     * @param targetList selected targets
     * @return response
     */
    ServerGameMessage handleTarget(MovementEffect movementEffect, List<Target> targetList);

    /**
     * Handle the target selection fo a plainDamageEffect, proceeding with its application
     * @param plainDamageEffect effect
     * @param targetList selected targets
     * @return response
     */
    ServerGameMessage handleTarget(PlainDamageEffect plainDamageEffect, List<Target> targetList);

    /**
     * Handle the target selection fo a plainDamageEffect, proceeding with its application
     * @param areaDamageEffect effect
     * @param targetList selected targets
     * @return response
     */
    ServerGameMessage handleTarget(AreaDamageEffect areaDamageEffect, List<Target> targetList);
    /**
     * Handle the target selection fo a plainDamageEffect, proceeding with its application
     * @param squareDamageEffect effect
     * @param targetList selected targets
     * @return response
     */
    ServerGameMessage handleTarget(SquareDamageEffect squareDamageEffect, List<Target> targetList);
    /**
     * Handle the target selection fo a plainDamageEffect, proceeding with its application
     * @param roomDamageEffect effect
     * @param targetList selected targets
     * @return response
     */
    ServerGameMessage handleTarget(RoomDamageEffect roomDamageEffect, List<Target> targetList);
}
