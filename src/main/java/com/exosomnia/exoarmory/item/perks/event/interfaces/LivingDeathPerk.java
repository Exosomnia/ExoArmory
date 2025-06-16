package com.exosomnia.exoarmory.item.perks.event.interfaces;

import com.exosomnia.exoarmory.item.perks.event.handlers.PerkHandler;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public interface LivingDeathPerk extends PerkEvent {

    boolean livingDeathEvent(PerkHandler.Context<LivingDeathEvent> context);

}
