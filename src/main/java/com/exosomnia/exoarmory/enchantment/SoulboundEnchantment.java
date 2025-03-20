package com.exosomnia.exoarmory.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SoulboundEnchantment extends Enchantment {
    public SoulboundEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.create("any", (item) -> true), EquipmentSlot.values());
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    public int getMinCost(int level) {
        return level * 25;
    }

    public int getMaxCost(int level) {
        return this.getMinCost(level) + 50;
    }

    @Override
    public boolean canEnchant(ItemStack itemStack) { return itemStack.isEnchantable(); }
}
