package com.exosomnia.exoarmory.item.ability.event.handlers;

import com.exosomnia.exoarmory.item.ability.event.interfaces.AbilityEvent;
import com.exosomnia.exoarmory.item.ability.event.interfaces.CriticalHitAbility;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CriticalHitAbilityHandler extends AbilityHandler<CriticalHitEvent> {

    @SubscribeEvent
    public void criticalHitAbility(CriticalHitEvent event) {
        Player player = event.getEntity();
        handleEvent(event, player);
    }

    @Override
    protected Class<? extends AbilityEvent<CriticalHitEvent>> getInterface() {
        return CriticalHitAbility.class;
    }
}
