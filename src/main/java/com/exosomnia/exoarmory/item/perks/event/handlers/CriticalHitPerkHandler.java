package com.exosomnia.exoarmory.item.perks.event.handlers;

import com.exosomnia.exoarmory.item.perks.event.interfaces.CriticalHitPerk;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;

public class CriticalHitPerkHandler extends PerkHandler<CriticalHitEvent, CriticalHitPerk> {

    public CriticalHitPerkHandler() {
        super(CriticalHitPerk.class, CriticalHitPerk::criticalHitEvent);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void criticalHitPerk(CriticalHitEvent event) {
        if (event.isCanceled()) return;

        HashSet<LivingEntity> involvedEntities = new HashSet<>();
        involvedEntities.add(event.getEntity());
        if (event.getTarget() instanceof LivingEntity targetEntity) involvedEntities.add(targetEntity);

        handleEvent(event, involvedEntities);
    }

}
