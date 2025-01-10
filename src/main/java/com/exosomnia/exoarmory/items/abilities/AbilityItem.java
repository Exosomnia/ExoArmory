package com.exosomnia.exoarmory.items.abilities;

import net.minecraft.world.item.ItemStack;

public interface AbilityItem {

    public ArmoryAbility[] getAbilities(ItemStack itemStack);

}
