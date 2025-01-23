package com.exosomnia.exoarmory.handlers;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingHealEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExoArmory.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CombatEventsHandler {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityHeal(LivingHealEvent event) {
        event.setAmount(event.getAmount() * (float)event.getEntity().getAttributeValue(ExoArmory.REGISTRY.ATTRIBUTE_HEALING_RECEIVED.get()));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onEntityHurt(LivingHurtEvent event) {
        float modifier = 1.0F - (float)event.getEntity().getAttributeValue(ExoArmory.REGISTRY.ATTRIBUTE_VULNERABILITY.get());
        event.setAmount(event.getAmount() + (event.getAmount() * modifier));
    }
}
