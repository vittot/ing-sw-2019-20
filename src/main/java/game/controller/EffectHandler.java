package game.controller;

import game.controller.commands.ServerMessage;
import game.model.Target;
import game.model.effects.*;

import java.util.List;

public interface EffectHandler {

    ServerMessage handle(MovementEffect e);
    ServerMessage handle(PlainDamageEffect e);
    ServerMessage handle(SquareDamageEffect e);
    ServerMessage handle(RoomDamageEffect e);
    ServerMessage handle(AreaDamageEffect e);
    ServerMessage handleTarget(MovementEffect movementEffect, List<Target> targetList);
    ServerMessage handleTarget(PlainDamageEffect plainDamageEffect, List<Target> targetList);
    ServerMessage handleTarget(AreaDamageEffect areaDamageEffect, List<Target> targetList);
    ServerMessage handleTarget(SquareDamageEffect squareDamageEffect, List<Target> targetList);
    ServerMessage handleTarget(RoomDamageEffect roomDamageEffect, List<Target> targetList);
}
