package com.exosomnia.exoarmory.mixin.mixins;

import com.exosomnia.exoarmory.mixin.interfaces.IItemMixin;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Item.class)
public class ItemMixin implements IItemMixin {

    public void writeItemNetworkData(ItemStack itemStack, FriendlyByteBuf buffer) {}

    public void readItemNetworkData(ItemStack itemStack, FriendlyByteBuf buffer) {}

    public boolean shouldResendData(ItemStack itemStack, ItemStack otherStack) {
        return false;
    }

    public boolean ignoreCapabilitiesForMatch() {
        return false;
    }
}
