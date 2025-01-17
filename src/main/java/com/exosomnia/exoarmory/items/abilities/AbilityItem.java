package com.exosomnia.exoarmory.items.abilities;

import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface AbilityItem {

    public List<ArmoryAbility> getAbilities(ItemStack itemStack);
    public default <T extends ArmoryAbility> T getAbility(T ability, ItemStack itemStack, int rank) {
        return getAbilities(itemStack).contains(ability) ? ability : null;
    }

}
