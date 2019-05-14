package game.controller.commands.clientcommands;

import game.controller.commands.ClientMessage;
import game.controller.commands.ClientMessageHandler;
import game.controller.commands.ServerMessage;
import game.model.CardPower;
import game.model.CardWeapon;
import game.model.effects.FullEffect;

import java.util.List;

public class ShootActionRequest implements ClientMessage {
    private CardWeapon weapon;
    private List<FullEffect> plusEffects;
    private FullEffect baseEffect;
    private FullEffect altEffect;
    private List<CardPower> paymentWithPowerUp;
    /**
     * If this is true the weapon has to have one (and only one) plus effect which must be executed before the base effect and this has to be the first in the plusEffectsList
     */
    private boolean plusBeforeBase;

    @Override
    public ServerMessage handle(ClientMessageHandler handler) {
        return handler.handle(this);
    }

    public CardWeapon getWeapon() {
        return weapon;
    }

    public List<FullEffect> getPlusEffects() {
        return plusEffects;
    }

    public FullEffect getBaseEffect() {
        return baseEffect;
    }

    public FullEffect getAltEffect() {
        return altEffect;
    }

    public List<CardPower> getPaymentWithPowerUp() {
        return paymentWithPowerUp;
    }

    public boolean isPlusBeforeBase() {
        return plusBeforeBase;
    }
}
