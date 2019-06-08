package game.controller;

import game.controller.commands.ServerMessage;
import game.model.effects.*;

public interface EffectHandler {

    ServerMessage handle(MovementEffect e);
    ServerMessage handle(PlainDamageEffect e);
    ServerMessage handle(SquareDamageEffect e);
    ServerMessage handle(RoomDamageEffect e);
    ServerMessage handle(AreaDamageEffect e);
}
