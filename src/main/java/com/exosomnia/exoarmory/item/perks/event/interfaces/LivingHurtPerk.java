package com.exosomnia.exoarmory.item.perks.event.interfaces;

import com.exosomnia.exoarmory.item.perks.event.handlers.PerkHandler;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public interface LivingHurtPerk extends PerkEvent<LivingHurtEvent> {

    boolean livingHurtEvent(PerkHandler.Context<LivingHurtEvent> context);

    default boolean handle(PerkHandler.Context<LivingHurtEvent> context) {
        return livingHurtEvent(context);
    }
}
