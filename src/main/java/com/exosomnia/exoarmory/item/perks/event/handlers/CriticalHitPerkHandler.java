package com.exosomnia.exoarmory.item.perks.event.handlers;

import com.exosomnia.exoarmory.item.perks.event.interfaces.PerkEvent;
import com.exosomnia.exoarmory.item.perks.event.interfaces.CriticalHitPerk;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CriticalHitPerkHandler extends PerkHandler<CriticalHitEvent> {

    @SubscribeEvent
    public void criticalHitAbility(CriticalHitEvent event) {
        Player player = event.getEntity();
        handleEvent(event, player);
    }

    @Override
    protected Class<? extends PerkEvent<CriticalHitEvent>> getInterface() {
        return CriticalHitPerk.class;
    }
}
