package com.exosomnia.exoarmory.item.ability.event.handlers;

import com.exosomnia.exoarmory.item.ability.event.interfaces.AbilityEvent;
import com.exosomnia.exoarmory.item.ability.event.interfaces.LivingHurtAbility;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LivingHurtAbilityHandler extends AbilityHandler<LivingHurtEvent> {

    @SubscribeEvent
    public void livingHurtAbility(LivingHurtEvent event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;
        handleEvent(event, attacker);
    }

    @Override
    protected Class<? extends AbilityEvent<LivingHurtEvent>> getInterface() {
        return LivingHurtAbility.class;
    }
}
