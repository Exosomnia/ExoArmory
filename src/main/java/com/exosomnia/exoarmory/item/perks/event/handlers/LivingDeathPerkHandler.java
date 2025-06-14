package com.exosomnia.exoarmory.item.perks.event.handlers;

import com.exosomnia.exoarmory.item.perks.event.interfaces.PerkEvent;
import com.exosomnia.exoarmory.item.perks.event.interfaces.LivingDeathPerk;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LivingDeathPerkHandler extends PerkHandler<LivingDeathEvent> {

    @SubscribeEvent
    public void livingHurtAbility(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;
        handleEvent(event, attacker);
    }

    @Override
    protected Class<? extends PerkEvent<LivingDeathEvent>> getInterface() {
        return LivingDeathPerk.class;
    }
}
