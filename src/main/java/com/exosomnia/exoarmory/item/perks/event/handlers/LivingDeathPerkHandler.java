package com.exosomnia.exoarmory.item.perks.event.handlers;

import com.exosomnia.exoarmory.item.perks.event.interfaces.LivingDeathPerk;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;

public class LivingDeathPerkHandler extends PerkHandler<LivingDeathEvent, LivingDeathPerk> {

    public LivingDeathPerkHandler() {
        super(LivingDeathPerk.class, LivingDeathPerk::livingDeathEvent);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void livingHurtPerk(LivingDeathEvent event) {
        if (event.isCanceled()) return;

        HashSet<LivingEntity> involvedEntities = new HashSet<>();
        if (event.getSource().getEntity() instanceof LivingEntity attacker) involvedEntities.add(attacker);
        involvedEntities.add(event.getEntity());

        handleEvent(event, involvedEntities);
    }

}
