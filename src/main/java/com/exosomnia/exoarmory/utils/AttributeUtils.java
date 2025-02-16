package com.exosomnia.exoarmory.utils;

import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class AttributeUtils {

    public static double getAttributeValueOfItemStack(Attribute attribute, EquipmentSlot slot, ItemStack itemStack, double base) {
        Multimap<Attribute, AttributeModifier> attributes = itemStack.getAttributeModifiers(slot);
        if (attributes.isEmpty()) { return base; }

        Collection<AttributeModifier> modifiers = attributes.get(attribute);
        double add = 0;
        double multiBase = 1;
        double multiTotal = 1;
        for (AttributeModifier mod : modifiers) {
            switch(mod.getOperation()) {
                case ADDITION -> add += mod.getAmount();
                case MULTIPLY_BASE -> add += mod.getAmount();
                case MULTIPLY_TOTAL -> add += mod.getAmount();
            }
        }
        return ((base * multiBase) + add) * multiTotal;
    }

}
