package game.controller;

import game.controller.commands.ServerGameMessage;
import game.model.Target;
import game.model.effects.*;

import java.util.List;

public interface EffectHandler {

    ServerGameMessage handle(MovementEffect e);
    ServerGameMessage handle(PlainDamageEffect e);
    ServerGameMessage handle(SquareDamageEffect e);
    ServerGameMessage handle(RoomDamageEffect e);
    ServerGameMessage handle(AreaDamageEffect e);
    ServerGameMessage handleTarget(MovementEffect movementEffect, List<Target> targetList);
    ServerGameMessage handleTarget(PlainDamageEffect plainDamageEffect, List<Target> targetList);
    ServerGameMessage handleTarget(AreaDamageEffect areaDamageEffect, List<Target> targetList);
    ServerGameMessage handleTarget(SquareDamageEffect squareDamageEffect, List<Target> targetList);
    ServerGameMessage handleTarget(RoomDamageEffect roomDamageEffect, List<Target> targetList);
}
