package com.exosomnia.exoarmory.item.ability.event.interfaces;

import com.exosomnia.exoarmory.item.ability.event.handlers.AbilityHandler;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public interface LivingDeathAbility extends AbilityEvent<LivingDeathEvent> {

    boolean livingDeathEvent(AbilityHandler.Context<LivingDeathEvent> event);

    default boolean handle(AbilityHandler.Context<LivingDeathEvent> event) {
        return livingDeathEvent(event);
    }
}
