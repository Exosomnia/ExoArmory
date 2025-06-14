package com.exosomnia.exoarmory.item.perks.event.interfaces;

import com.exosomnia.exoarmory.item.perks.event.handlers.PerkHandler;
import net.minecraftforge.event.entity.player.CriticalHitEvent;

public interface CriticalHitPerk extends PerkEvent<CriticalHitEvent> {

    boolean criticalHitEvent(PerkHandler.Context<CriticalHitEvent> context);

    @Override
    default boolean handle(PerkHandler.Context<CriticalHitEvent> context) {
        return criticalHitEvent(context);
    }
}
