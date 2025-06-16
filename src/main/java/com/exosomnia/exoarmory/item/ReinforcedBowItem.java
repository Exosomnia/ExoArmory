package com.exosomnia.exoarmory.item;

import com.exosomnia.exoarmory.ExoArmory;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

public class ReinforcedBowItem extends BowItem {

    private static final UUID RANGED_STRENGTH_UUID = UUID.fromString("4477081e-59b2-4cba-9613-07ac68cd0d5b");
    private static final UUID PIERCE_UUID = UUID.fromString("039f2ff0-52d8-49c8-9de4-1b3892aad343");
    private final double strength;
    private final double pierce;

    public ReinforcedBowItem(Item.Properties properties, double strength, double pierce) {
        super(properties);
        this.strength = strength;
        this.pierce = pierce;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack itemStack) {
        if (slot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> attributes = new ImmutableMultimap.Builder<>();
            attributes.put(ExoArmory.REGISTRY.ATTRIBUTE_RANGED_STRENGTH.get(),
                    new AttributeModifier(RANGED_STRENGTH_UUID, "Default", strength, AttributeModifier.Operation.MULTIPLY_BASE));
            if (pierce > 0) {
                attributes.put(ExoArmory.REGISTRY.ATTRIBUTE_ARROW_PIERCE.get(),
                        new AttributeModifier(PIERCE_UUID, "Default", pierce, AttributeModifier.Operation.MULTIPLY_BASE));
            }
            return attributes.build();
        }
        return ImmutableMultimap.of();
    }

    @Override
    public int getEnchantmentValue() {
        return 6;
    }

    public static float getPowerForTime(int p_40662_) {
        float f = (float)p_40662_ / 16.0F;
        f = (f * f + f * 2.0F) / 3.0F;
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }
}
