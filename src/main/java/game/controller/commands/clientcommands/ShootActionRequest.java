package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.CardPower;
import game.model.CardWeapon;
import game.model.effects.FullEffect;

import java.util.List;

public class ShootActionRequest implements ClientMessage {
    public CardWeapon weapon;
    public List<FullEffect> plusEffects;
    public FullEffect baseEffect;
    public FullEffect altEffect;
    public List<CardPower> paymentWithPowerUp;
    /**
     * If this is true the weapon has to have one (and only one) plus effect which must be executed before the base effect and this has to be the first in the plusEffectsList
     */
    public boolean plusBeforeBase;

    @Override
    public ServerMessage handle(ClientMessageHandler handler) {
        return handler.handle(this);
    }
}
