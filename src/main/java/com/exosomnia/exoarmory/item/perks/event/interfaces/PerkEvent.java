package com.exosomnia.exoarmory.item.perks.event.interfaces;

import com.exosomnia.exoarmory.item.perks.event.handlers.PerkHandler;
import net.minecraftforge.eventbus.api.Event;

public interface PerkEvent<T extends Event> {

    boolean handle(PerkHandler.Context<T> context);
}
