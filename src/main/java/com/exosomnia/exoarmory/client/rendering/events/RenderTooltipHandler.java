package com.exosomnia.exoarmory.client.rendering.events;

import com.exosomnia.exoarmory.ExoArmory;
import com.mojang.datafixers.util.Either;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderTooltipEvent;
import net.minecraftforge.common.ToolActions;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber(modid = ExoArmory.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RenderTooltipHandler {

    @SubscribeEvent
    public static void tooltipRenderEvent(RenderTooltipEvent.GatherComponents event) {
        ItemStack stack = event.getItemStack();
        if (!stack.canPerformAction(ToolActions.SHIELD_BLOCK) || !stack.getAttributeModifiers(EquipmentSlot.MAINHAND).keys().equals(stack.getAttributeModifiers(EquipmentSlot.OFFHAND).keys())) { return; }
        List<Either<FormattedText, TooltipComponent>> elements = event.getTooltipElements();
        int count = elements.size();
        int mainHandStart = 2;
        int offHandStart = 2;
        for (var i = 2; i < count; i++) {
            Optional<FormattedText> text = elements.get(i).left();
            if (text.isPresent()) {
                if (text.get().getString().equals(I18n.get("item.modifiers.mainhand"))) {
                    mainHandStart = i;
                }
                else if (text.get().getString().equals(I18n.get("item.modifiers.offhand"))) {
                    offHandStart = i;
                    break;
                }
            }
        }

        if (offHandStart > mainHandStart) {
            int linesBetween = offHandStart - mainHandStart;
            for (var ii = 0; ii <= linesBetween; ii++) {
                elements.remove(mainHandStart);
            }
            elements.add(mainHandStart, Either.left(Component.translatable("item.modifiers.bothhand").withStyle(ChatFormatting.GRAY)));
        }
    }
}
