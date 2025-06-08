package com.exosomnia.exoarmory.item.ability.event.interfaces;

import com.exosomnia.exoarmory.item.ability.event.handlers.AbilityHandler;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public interface LivingHurtAbility extends AbilityEvent<LivingHurtEvent> {

    boolean livingHurtEvent(AbilityHandler.Context<LivingHurtEvent> event);

    default boolean handle(AbilityHandler.Context<LivingHurtEvent> event) {
        return livingHurtEvent(event);
    }
}
