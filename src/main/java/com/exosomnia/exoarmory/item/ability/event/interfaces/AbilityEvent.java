package com.exosomnia.exoarmory.item.ability.event.interfaces;

import com.exosomnia.exoarmory.item.ability.event.handlers.AbilityHandler;
import net.minecraftforge.eventbus.api.Event;

public interface AbilityEvent<T extends Event> {

    boolean handle(AbilityHandler.Context<T> eventContext);
}
