package com.exosomnia.exoarmory.item.perks.event.handlers;

import com.exosomnia.exoarmory.item.perks.event.interfaces.PerkEvent;
import com.exosomnia.exoarmory.item.perks.event.interfaces.LivingHurtPerk;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LivingHurtPerkHandler extends PerkHandler<LivingHurtEvent> {

    @SubscribeEvent
    public void livingHurtAbility(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;
        handleEvent(event, attacker);
    }

    @Override
    protected Class<? extends PerkEvent<LivingHurtEvent>> getInterface() {
        return LivingHurtPerk.class;
    }
}
