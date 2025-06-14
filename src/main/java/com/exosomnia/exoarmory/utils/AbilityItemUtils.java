package com.exosomnia.exoarmory.utils;

import com.exosomnia.exoarmory.capabilities.armory.item.ability.ArmoryAbilityProvider;
import com.exosomnia.exoarmory.capabilities.armory.item.ability.ArmoryAbilityStorage;
import com.exosomnia.exoarmory.item.perks.ability.AbilityItem;
import com.exosomnia.exoarmory.item.perks.ability.ArmoryAbility;
import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntLinkedOpenHashMap;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public class AbilityItemUtils {

    /**
     * Gets the ability rank of the specified ability on the provided ItemStack.
     * ability rank is calculated as, (Ability Entry + Specific Ability Rank)
     * @param itemStack the ItemStack to get the ability rank from
     * @return The ability rank on the ItemStack, or 0 if rank and ability data doesn't exist
     */
    public static int getAbilityRank(ArmoryAbility ability, ItemStack itemStack, LivingEntity wielder) {
        Optional<ArmoryAbilityStorage> abilityCapability = itemStack.getCapability(ArmoryAbilityProvider.ARMORY_ABILITY).resolve();
        int abilityEntryRank = (itemStack.getItem() instanceof AbilityItem abilityItem) ? abilityItem.getAbilities(itemStack, wielder).getOrDefault(ability, 0) : 0;
        return (abilityEntryRank + (abilityCapability.isPresent() ? abilityCapability.get().getAbilityBonus(ability) : 0));
    }

    public static ImmutableMap<ArmoryAbility, Integer> createAbilityMapping(ArmoryAbility.Entry... abilityEntries) {
        ImmutableMap.Builder<ArmoryAbility, Integer> abilityMapBuilder = new ImmutableMap.Builder<>();
        for (ArmoryAbility.Entry entry : abilityEntries) {
            abilityMapBuilder.put(entry.ability(), entry.rank());
        }
        return abilityMapBuilder.build();
    }

    public static AbilityRanksBuilder rankBuilder() {
        return new AbilityRanksBuilder();
    }

    public static class AbilityRanksBuilder {

        private final ImmutableMap.Builder<ArmoryAbility, Integer> builder = new ImmutableMap.Builder<>();

        public AbilityRanksBuilder addAbility(ArmoryAbility ability, int rank) {
            builder.put(ability, rank);
            return this;
        }

        public Object2IntLinkedOpenHashMap<ArmoryAbility> build() {
            return new Object2IntLinkedOpenHashMap<>(builder.build());
        }
    }
}
