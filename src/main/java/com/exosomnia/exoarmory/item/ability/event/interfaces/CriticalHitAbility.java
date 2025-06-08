package com.exosomnia.exoarmory.item.ability.event.interfaces;

import com.exosomnia.exoarmory.item.ability.event.handlers.AbilityHandler;
import net.minecraftforge.event.entity.player.CriticalHitEvent;

public interface CriticalHitAbility extends AbilityEvent<CriticalHitEvent> {

    boolean criticalHitEvent(AbilityHandler.Context<CriticalHitEvent> event);

    @Override
    default boolean handle(AbilityHandler.Context<CriticalHitEvent> event) {
        return criticalHitEvent(event);
    }
}
