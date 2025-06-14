package com.exosomnia.exoarmory.item.armory;

import com.exosomnia.exoarmory.mixin.interfaces.IItemMixin;
import com.exosomnia.exoarmory.utils.ArmoryItemUtils;
import com.exosomnia.exolib.utils.ComponentUtils.DetailLevel;
import com.google.common.collect.Multimap;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeItem;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public interface ArmoryItem extends IForgeItem, IItemMixin {

    //region ArmoryTag methods
    default UUID getUUID(ItemStack itemStack) { return ArmoryItemUtils.getUUID(itemStack); }

    default int getRank(ItemStack itemStack) { return ArmoryItemUtils.getRank(itemStack); }

    default void setRank(ItemStack itemStack, int rank) { ArmoryItemUtils.setRank(itemStack, rank); }
    //endregion

    void addToHover(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag, int rank, DetailLevel detail);

    Multimap<Attribute, AttributeModifier>[] getAttributesForAllRanks();

    default Multimap<Attribute, AttributeModifier> getAttributesForRank(int rank) {
        return getAttributesForAllRanks()[Math.min(4, Math.max(0, rank))];
    }
}
