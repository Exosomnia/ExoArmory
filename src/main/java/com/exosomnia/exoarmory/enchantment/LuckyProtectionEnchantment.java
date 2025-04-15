package com.exosomnia.exoarmory.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.ProtectionEnchantment;
import net.minecraftforge.common.Tags;

public class LuckyProtectionEnchantment extends Enchantment {
    public LuckyProtectionEnchantment() {
        super(Rarity.VERY_RARE, EnchantmentCategory.ARMOR, EquipmentSlot.values());
    }

    @Override
    public boolean isTradeable() {
        return false;
    }

    @Override
    public boolean isDiscoverable() {
        return false;
    }

    public int getMinCost(int level) {
        return 100;
    }

    public int getMaxCost(int level) {
        return 100;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) { return false; }

    @Override
    public boolean checkCompatibility(Enchantment enchantment) {
        if (enchantment instanceof ProtectionEnchantment protectionEnchantment) {
            return protectionEnchantment.type == ProtectionEnchantment.Type.FALL;
        }
        else {
            return super.checkCompatibility(enchantment);
        }
    }
}
