package game.controller;

import game.controller.commands.ServerMessage;
import game.model.effects.MovementEffect;
import game.model.effects.PlainDamageEffect;
import game.model.effects.RoomDamageEffect;
import game.model.effects.SquareDamageEffect;

public interface EffectHandler {

    ServerMessage handle(MovementEffect e);
    ServerMessage handle(PlainDamageEffect e);
    ServerMessage handle(SquareDamageEffect e);
    ServerMessage handle(RoomDamageEffect e);
}
