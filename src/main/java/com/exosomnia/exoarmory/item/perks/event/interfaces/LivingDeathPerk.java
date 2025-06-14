package com.exosomnia.exoarmory.item.perks.event.interfaces;

import com.exosomnia.exoarmory.item.perks.event.handlers.PerkHandler;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public interface LivingDeathPerk extends PerkEvent<LivingDeathEvent> {

    boolean livingDeathEvent(PerkHandler.Context<LivingDeathEvent> context);

    default boolean handle(PerkHandler.Context<LivingDeathEvent> context) {
        return livingDeathEvent(context);
    }
}
