package com.exosomnia.exoarmory.client.rendering.events;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exoarmory.accessors.EntityAccessor;
import com.exosomnia.exoarmory.capabilities.aethersembrace.AethersEmbraceProvider;
import com.exosomnia.exoarmory.items.armory.bows.AethersEmbraceBow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ExoArmory.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RenderLivingHandler {

    @SubscribeEvent
    public static void levelRender(RenderLevelStageEvent event) {
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_SKY)) {
            Minecraft mc = Minecraft.getInstance();
            ClientLevel level = mc.level;

            if (level == null) return;

            ItemStack itemStack = mc.player.getMainHandItem();
            if (itemStack.getItem() instanceof AethersEmbraceBow bow) {
                itemStack.getCapability(AethersEmbraceProvider.AETHERS_EMBRACE).ifPresent(data -> {
                    if (bow.isTargeting(itemStack, level)) {
                        for (Entity entity : level.entitiesForRendering()) {
                            if (entity.getUUID().equals(data.getTarget())) {
                                ((EntityAccessor) entity).setClientGlowing(true);
                                break;
                            }
                        }
                    }
                });
            }
        }
    }

    @SubscribeEvent
    public static void livingRender(RenderLivingEvent<LivingEntity, ?> event) {
        MultiBufferSource buffSource = event.getMultiBufferSource();
        if (buffSource instanceof OutlineBufferSource outlineSource) {
            outlineSource.setColor(245, 198, 69, 255);
            ((EntityAccessor)event.getEntity()).setClientGlowing(false);
        }
    }
}
