package com.exosomnia.exoarmory.enchantment;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.ToolActions;

public class FortifyingEnchantment extends Enchantment {
    public FortifyingEnchantment() {
        super(Rarity.UNCOMMON, EnchantmentCategory.WEARABLE, new EquipmentSlot[]{EquipmentSlot.OFFHAND});
    }

    public boolean isTreasureOnly() {
        return true;
    }

    public boolean isCurse() {
        return false;
    }

    public boolean isTradeable() {
        return false;
    }

    public boolean isDiscoverable() {
        return true;
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchantment) { return !enchantment.equals(ExoArmory.REGISTRY.ENCHANTMENT_RALLYING.get()); }

    @Override
    public boolean canEnchant(ItemStack itemStack) { return itemStack.canPerformAction(ToolActions.SHIELD_BLOCK); }
}
