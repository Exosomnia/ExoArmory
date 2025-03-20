package com.exosomnia.exoarmory.enchantment;

import com.exosomnia.exoarmory.ExoArmory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.common.ToolActions;

public class RallyingEnchantment extends Enchantment {
    public RallyingEnchantment() {
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

    public int getMinCost(int level) {
        return level * 25;
    }

    public int getMaxCost(int level) {
        return this.getMinCost(level) + 50;
    }

    @Override
    protected boolean checkCompatibility(Enchantment enchantment) { return !(enchantment instanceof FortifyingEnchantment) && super.checkCompatibility(enchantment); }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack itemStack) { return itemStack.canPerformAction(ToolActions.SHIELD_BLOCK); }
}
