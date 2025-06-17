package com.exosomnia.exoarmory.item;

import com.exosomnia.exoarmory.ExoArmory;
import com.exosomnia.exolib.utils.ComponentUtils;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class ReinforcedBowItem extends BowItem {

    private static final UUID RANGED_STRENGTH_UUID = UUID.fromString("4477081e-59b2-4cba-9613-07ac68cd0d5b");
    private static final UUID PIERCE_UUID = UUID.fromString("039f2ff0-52d8-49c8-9de4-1b3892aad343");
    private static final UUID RECOVERY_UUID = UUID.fromString("1e88dac8-4f7b-43e1-ba04-fbbbfd419daf");
    private final double strength;
    private final double pierce;
    private final double recovery;
    private final boolean drawSpeed;

    Multimap<Attribute, AttributeModifier> attributes;

    public ReinforcedBowItem(Item.Properties properties, double strength, double pierce, double recovery, boolean drawSpeed) {
        super(properties);
        this.strength = strength;
        this.pierce = pierce;
        this.recovery = recovery;
        this.drawSpeed = drawSpeed;

        ImmutableMultimap.Builder<Attribute, AttributeModifier> attributesBuilder = new ImmutableMultimap.Builder<>();
        attributesBuilder.put(ExoArmory.REGISTRY.ATTRIBUTE_RANGED_STRENGTH.get(),
                new AttributeModifier(RANGED_STRENGTH_UUID, "Default", strength, AttributeModifier.Operation.MULTIPLY_BASE));
        if (pierce > 0) {
            attributesBuilder.put(ExoArmory.REGISTRY.ATTRIBUTE_ARROW_PIERCE.get(),
                    new AttributeModifier(PIERCE_UUID, "Default", pierce, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        if (recovery > 0) {
            attributesBuilder.put(ExoArmory.REGISTRY.ATTRIBUTE_ARROW_RECOVERY.get(),
                    new AttributeModifier(RECOVERY_UUID, "Default", recovery, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        attributes = attributesBuilder.build();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack itemStack) {
        return slot == EquipmentSlot.MAINHAND ? attributes : ImmutableMultimap.of();
    }

    @Override
    public int getEnchantmentValue() {
        return 6;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag) {
        if (!drawSpeed) return;
        components.add(Component.translatable("item.reinforced_bow.draw_speed.bonus").withStyle(ComponentUtils.Styles.BLANK.getStyle()
                .withItalic(true).withColor(ChatFormatting.DARK_GRAY)));
    }

    public static float getPowerForTime(ReinforcedBowItem item, int p_40662_) {
        float f = (float)p_40662_ / (item.drawSpeed ? 16.0F : 20.0F);
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }
}
