package com.exosomnia.exoarmory.utils;

import com.exosomnia.exoarmory.capabilities.armory.item.ability.ArmoryAbilityProvider;
import com.exosomnia.exoarmory.capabilities.armory.item.ability.ArmoryAbilityStorage;
import com.exosomnia.exoarmory.item.ability.ArmoryAbility;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class AbilityItemUtils {

    /**
     * Gets the ability rank of the specified ability on the provided ItemStack.
     * ability rank is calculated as, (Armory Rank + Specific Ability Rank)
     * @param itemStack the ItemStack to get the ability rank from
     * @return The ability rank on the ItemStack, or 0 if rank and ability data doesn't exist
     */
    public static int getAbilityRank(ArmoryAbility ability, ItemStack itemStack) {
        Optional<ArmoryAbilityStorage> abilityCapability = itemStack.getCapability(ArmoryAbilityProvider.ARMORY_ABILITY).resolve();
        return (ArmoryItemUtils.getRank(itemStack) + (abilityCapability.isPresent() ? abilityCapability.get().getAbilityBonus(ability) : 0));
    }
}
