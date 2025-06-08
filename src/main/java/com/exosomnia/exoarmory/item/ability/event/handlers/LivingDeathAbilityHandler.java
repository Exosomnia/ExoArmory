package com.exosomnia.exoarmory.item.ability.event.handlers;

import com.exosomnia.exoarmory.item.ability.event.interfaces.AbilityEvent;
import com.exosomnia.exoarmory.item.ability.event.interfaces.LivingDeathAbility;
import com.exosomnia.exoarmory.item.ability.event.interfaces.LivingHurtAbility;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class LivingDeathAbilityHandler extends AbilityHandler<LivingDeathEvent> {

    @SubscribeEvent
    public void livingHurtAbility(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof LivingEntity attacker)) return;
        handleEvent(event, attacker);
    }

    @Override
    protected Class<? extends AbilityEvent<LivingDeathEvent>> getInterface() {
        return LivingDeathAbility.class;
    }
}
