package com.exosomnia.exoarmory.mixin.interfaces;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;

public interface IItemMixin {

    void writeItemNetworkData(ItemStack itemStack, FriendlyByteBuf buffer);

    void readItemNetworkData(ItemStack itemStack, FriendlyByteBuf buffer);

    boolean shouldResendData(ItemStack itemStack, ItemStack otherStack);

    boolean ignoreCapabilitiesForMatch();
}
