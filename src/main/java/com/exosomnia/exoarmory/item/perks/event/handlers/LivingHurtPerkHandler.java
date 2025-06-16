package com.exosomnia.exoarmory.item.perks.event.handlers;

import com.exosomnia.exoarmory.item.perks.event.interfaces.LivingHurtPerk;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;

public class LivingHurtPerkHandler extends PerkHandler<LivingHurtEvent, LivingHurtPerk> {

    public LivingHurtPerkHandler() {
        super(LivingHurtPerk.class, LivingHurtPerk::livingHurtEvent);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void livingHurtPerk(LivingHurtEvent event) {
        if (event.isCanceled()) return;

        HashSet<LivingEntity> involvedEntities = new HashSet<>();
        if (event.getSource().getEntity() instanceof LivingEntity attacker) involvedEntities.add(attacker);
        involvedEntities.add(event.getEntity());

        handleEvent(event, involvedEntities);
    }
}
