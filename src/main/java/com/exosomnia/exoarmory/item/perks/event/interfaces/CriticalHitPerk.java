package com.exosomnia.exoarmory.item.perks.event.interfaces;

import com.exosomnia.exoarmory.item.perks.event.handlers.PerkHandler;
import net.minecraftforge.event.entity.player.CriticalHitEvent;

public interface CriticalHitPerk extends PerkEvent {

    boolean criticalHitEvent(PerkHandler.Context<CriticalHitEvent> context);

}
