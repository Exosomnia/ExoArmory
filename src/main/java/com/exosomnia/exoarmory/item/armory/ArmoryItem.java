package com.exosomnia.exoarmory.item.armory;

import com.exosomnia.exoarmory.mixin.interfaces.IItemMixin;
import com.exosomnia.exolib.utils.ComponentUtils.DetailLevel;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
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
    default CompoundTag getArmoryTag(ItemStack itemStack) {
        CompoundTag rootTag = itemStack.getOrCreateTag();
        if (!rootTag.contains("ArmoryData")) {
            CompoundTag armoryTag = rootTag.getCompound("ArmoryData");
            armoryTag.putUUID("UUID", UUID.randomUUID());
            armoryTag.putInt("Rank", 0);
            rootTag.put("ArmoryData", armoryTag);
            return armoryTag;
        }
        return rootTag.getCompound("ArmoryData");
    }

    default UUID getUUID(ItemStack itemStack) { return getArmoryTag(itemStack).getUUID("UUID"); }

    default int getRank(ItemStack itemStack) { return getArmoryTag(itemStack).getInt("Rank"); }

    default void setRank(ItemStack itemStack, int rank) { getArmoryTag(itemStack).putInt("Rank", rank); }
    //endregion

    void addToHover(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag flag, int rank, DetailLevel detail);

    Multimap<Attribute, AttributeModifier>[] getAttributesForAllRanks();

    default Multimap<Attribute, AttributeModifier> getAttributesForRank(int rank) {
        return getAttributesForAllRanks()[Math.min(4, Math.max(0, rank))];
    }
}
