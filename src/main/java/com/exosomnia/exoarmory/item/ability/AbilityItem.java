package com.exosomnia.exoarmory.item.ability;

import com.exosomnia.exoarmory.item.armory.ArmoryItem;
import com.google.common.collect.ImmutableSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public interface AbilityItem extends ArmoryItem {

    ImmutableSet<ArmoryAbility> getAbilities(ItemStack itemStack, LivingEntity wielder);
}
